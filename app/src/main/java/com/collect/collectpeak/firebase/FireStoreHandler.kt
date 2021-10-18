package com.collect.collectpeak.firebase

import com.collect.collectpeak.fragment.equipment.equipment_select.*
import com.collect.collectpeak.fragment.member.MemberBasicData
import com.collect.collectpeak.fragment.member.MemberData
import com.collect.collectpeak.log.MichaelLog
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FireStoreHandler {

    private lateinit var firestore: FirebaseFirestore

    private val disposable = CompositeDisposable()

    private var userSelectEquipmentArray = ArrayList<EquipmentUserData>()

    companion object {

        private val instance = FireStoreHandler()

        const val API = "api"
        const val MOUNTAIN_LIST = "mountain_list"
        const val EQUIPMENT_LIST = "equipment_api"
        const val USER = "user"
        const val USER_LIST = "user_list"
        const val USER_PHOTO = "user_photo"
        const val USER_BASIC_INFO = "user_basic_info"
        const val USER_EQUIPMENT_LIST = "user_equipment_list"

        fun getInstance(): FireStoreHandler {
            return instance
        }


    }

    fun checkUserExistListener(
        user: FirebaseUser,
        onCheckUserExistResultListener: OnCheckUserExistResultListener
    ) {

        val documentReference = firestore.collection(USER).document(USER_LIST)

        documentReference.addSnapshotListener { value, error ->

            if (error != null) {

                onCheckUserExistResultListener.onFail()
                MichaelLog.i("錯誤 無法取的使用者資料")
                return@addSnapshotListener
            }

            if (value != null && value.exists()) {

                val json = value.get("user_json") as String

                if (json.isEmpty()) {
                    addFirstAccount(user, onCheckUserExistResultListener)
                    return@addSnapshotListener
                }
                val userList = Gson().fromJson<ArrayList<MemberData>>(
                    json,
                    object : TypeToken<ArrayList<MemberData>>() {}.type
                )

                if (userList.isNullOrEmpty()) {
                    onCheckUserExistResultListener.onFail()
                    return@addSnapshotListener
                }

                checkUserExist(user, userList, onCheckUserExistResultListener)


            }


        }


    }

    private fun checkUserExist(
        user:FirebaseUser,
        userList: java.util.ArrayList<MemberData>,
        onCheckUserExistResultListener: OnCheckUserExistResultListener
    ) {

        var isFoundUser = false
        for (data in userList) {
            if (data.userId == user.uid){
                isFoundUser = true
                break
            }
        }
        if (isFoundUser){
            onCheckUserExistResultListener.onUserExist()
            return
        }
        MichaelLog.i("新註冊帳號 先增加一筆空的")
        //創建第一筆資料
        addFirstBasicData()
        val memberData = MemberData()
        memberData.userId = user.uid
        memberData.email = user.email.toString()
        memberData.time = System.currentTimeMillis()
        userList.add(memberData)
        addUser(Gson().toJson(userList))
        onCheckUserExistResultListener.onNeedToAddNewProfile()



    }

    private fun addUser(toJson: String) {
        val map = HashMap<String, String>()

        map["user_json"] = toJson

        firestore.collection(USER)
            .document(USER_LIST)
            .set(map)
            .addOnCompleteListener {
                MichaelLog.i("新增一筆空的使用者資料完畢")
            }
    }

    private fun addFirstAccount(
        user: FirebaseUser,
        onCheckUserExistResultListener: OnCheckUserExistResultListener
    ) {
        MichaelLog.i("創建第一筆會員")
        //創建第一筆資料
        addFirstBasicData()

        val memberData = MemberData()
        memberData.userId = user.uid
        memberData.email = user.email.toString()
        memberData.time = System.currentTimeMillis()

        val userList = ArrayList<MemberData>()

        userList.add(memberData)

        val userJson = Gson().toJson(userList)

        val map = HashMap<String, String>()

        map["user_json"] = userJson

        firestore.collection(USER)
            .document(USER_LIST)
            .set(map)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onCheckUserExistResultListener.onNeedToAddNewProfile()
                } else {
                    onCheckUserExistResultListener.onFail()
                }
            }



    }
    private fun addFirstBasicData(){
        val memberBasicData = MemberBasicData()
        val json = Gson().toJson(memberBasicData)
        val map = HashMap<String,String>()
        map["json"] = json

        AuthHandler.getCurrentUser()?.uid?.let {
            firestore.collection(USER_BASIC_INFO)
                .document(it)
                .set(map, SetOptions.merge())
        }


    }

    fun initFireStore(firestore: FirebaseFirestore) {
        this.firestore = firestore
    }


    fun getMountainList(onFireStoreCatchDataListener: OnFireStoreCatchDataListener<ArrayList<MountainData>>) {

        firestore.collection(API)
            .document(MOUNTAIN_LIST)
            .get()
            .addOnCompleteListener { task ->
                if (!task.isSuccessful || task.result == null) {
                    MichaelLog.i("取得百岳資料失敗 : task")
                    onFireStoreCatchDataListener.onCatchDataFail()

                    return@addOnCompleteListener
                }
                val snapshot = task.result

                if (snapshot == null || snapshot.data == null) {
                    MichaelLog.i("取得百岳資料失敗 : snapshot")
                    onFireStoreCatchDataListener.onCatchDataFail()
                    return@addOnCompleteListener
                }

                val apiJson = snapshot.data?.get("json") as String

                val mtList = Gson().fromJson<ArrayList<MountainData>>(
                    apiJson,
                    object : TypeToken<ArrayList<MountainData>>() {}.type
                )

                MichaelLog.i("成功取得百岳列表 數量：${mtList.size}")

                onFireStoreCatchDataListener.onCatchDataSuccess(mtList)
            }

    }

    fun getEquipmentList(onFireStoreCatchDataListener: OnFireStoreCatchDataListener<EquipmentListData>) {
        firestore.collection(API)
            .document(EQUIPMENT_LIST)
            .get()
            .addOnCompleteListener { task ->
                if (!task.isSuccessful || task.result == null) {

                    MichaelLog.i("取得裝備清單失敗")
                    onFireStoreCatchDataListener.onCatchDataFail()

                    return@addOnCompleteListener
                }
                val snapshot = task.result

                if (snapshot == null || snapshot.data == null) {
                    MichaelLog.i("取得裝備失敗：snapshot")
                    onFireStoreCatchDataListener.onCatchDataFail()
                    return@addOnCompleteListener
                }

                val apiJson = snapshot.data?.get("json") as String

                MichaelLog.i("收到的 json : $apiJson")

                val equipmentList = Gson().fromJson<ArrayList<EquipmentOriginal>>(
                    apiJson,
                    object : TypeToken<ArrayList<EquipmentOriginal>>() {}.type
                )

                if (equipmentList.isNullOrEmpty()) {
                    MichaelLog.i("取得裝備清單失敗：equipmentList is null")
                    onFireStoreCatchDataListener.onCatchDataFail()
                    return@addOnCompleteListener
                }

                val equipmentTitle = ArrayList<String>()

                for (data in equipmentList) {
                    if (equipmentTitle.isEmpty()) {
                        equipmentTitle.add(data.sort)
                        continue
                    }
                    var isFoundDifferentSort = false
                    for (sort in equipmentTitle) {
                        if (sort == data.sort) {
                            isFoundDifferentSort = true
                            break
                        }
                    }
                    if (!isFoundDifferentSort) {
                        MichaelLog.i("加入的分類：${data.sort}")
                        equipmentTitle.add(data.sort)
                    }
                }
                MichaelLog.i("一共有 ${equipmentTitle.size} 分類")

                val replaceEquipmentList = ArrayList<EquipmentObject>()

                for (sort in equipmentTitle) {
                    val equipmentObject = EquipmentObject()
                    equipmentObject.title = sort
                    val dataArray = ArrayList<EquipmentData>()
                    for (data in equipmentList) {

                        if (sort == data.sort) {
                            val equData = EquipmentData()
                            equData.description = data.description
                            equData.name = data.name
                            equData.isCheck = false
                            dataArray.add(equData)
                        }

                    }
                    equipmentObject.equipmentData = dataArray
                    replaceEquipmentList.add(equipmentObject)
                }

                val indexArray = ArrayList<Int>()
                var index = 0

                for (i in 0 until replaceEquipmentList.size * 2) {

                    if (i != 0 && i % 2 == 0) {
                        index++
                    }
                    indexArray.add(index)
                    MichaelLog.i("index : $index")
                }

                val titleArray = ArrayList<String>()
                val equipmentItemList = ArrayList<ArrayList<EquipmentData>>()
                for (data in replaceEquipmentList) {
                    titleArray.add(data.title)
                    equipmentItemList.add(data.equipmentData)
                }


                val equipmentListData = EquipmentListData()

                equipmentListData.equipmentList = equipmentItemList
                equipmentListData.titleArray = titleArray
                equipmentListData.indexArray = indexArray


                var finalEquipmentListData = EquipmentListData()

                val observer = Observable
                    .just(equipmentListData)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

                observer.subscribe(object : Observer<EquipmentListData> {
                    override fun onSubscribe(d: Disposable) {
                        disposable.add(d)
                    }

                    override fun onNext(t: EquipmentListData) {
                        finalEquipmentListData = t
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {
                        onFireStoreCatchDataListener.onCatchDataSuccess(finalEquipmentListData)
                    }
                })
            }
    }

    fun setUserPhotoData(map: HashMap<String, String>) {

        AuthHandler.getCurrentUser()?.uid?.let {
            firestore.collection(USER_PHOTO)
                .document(it)
                .set(map, SetOptions.merge())
        }

    }

    fun getPhotoUrl(onFireStoreCatchDataListener: OnFireStoreCatchDataListener<String>) {

        AuthHandler.getCurrentUser()?.uid?.let {
            firestore.collection(USER_PHOTO)
                .document(it)
                .get()
                .addOnCompleteListener {task ->
                    if (!task.isSuccessful || task.result == null) {

                        MichaelLog.i("取得照片網址失敗")
                        onFireStoreCatchDataListener.onCatchDataFail()

                        return@addOnCompleteListener
                    }
                    val snapshot = task.result

                    if (snapshot == null || snapshot.data == null) {
                        MichaelLog.i("取得照片網址失敗：snapshot")
                        onFireStoreCatchDataListener.onCatchDataFail()
                        return@addOnCompleteListener
                    }

                    val photoUrl = snapshot.data?.get("photo") as String
                    onFireStoreCatchDataListener.onCatchDataSuccess(photoUrl)

                }
        }


    }

    fun getMemberBasicData(onFireStoreCatchDataListener: OnFireStoreCatchDataListener<MemberBasicData>) {

        AuthHandler.getCurrentUser()?.uid?.let {
            firestore.collection(USER_BASIC_INFO)
                .document(it)
                .get()
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful || task.result == null) {

                        MichaelLog.i("取得使用者基本資料失敗")
                        onFireStoreCatchDataListener.onCatchDataFail()

                        return@addOnCompleteListener
                    }
                    val snapshot = task.result

                    if (snapshot == null || snapshot.data == null) {
                        MichaelLog.i("取得使用者基本資料失敗：snapshot")
                        onFireStoreCatchDataListener.onCatchDataFail()
                        return@addOnCompleteListener
                    }

                    val json = snapshot.data?.get("json") as String

                    val basicData = Gson().fromJson(json,MemberBasicData::class.java)

                    onFireStoreCatchDataListener.onCatchDataSuccess(basicData)
                }
        }




    }

    fun getUserProfile(onFireStoreCatchDataListener: FireStoreHandler.OnFireStoreCatchDataListener<MemberData>) {

        firestore.collection(USER)
            .document(USER_LIST)
            .get()
            .addOnCompleteListener { task ->
                if (!task.isSuccessful && task.result == null){

                    onFireStoreCatchDataListener.onCatchDataFail()

                    return@addOnCompleteListener
                }
                val snapshot = task.result

                if (snapshot == null || snapshot.data == null){

                    onFireStoreCatchDataListener.onCatchDataFail()

                    return@addOnCompleteListener
                }


                val json = snapshot.data?.get("user_json") as String

                val userList = Gson().fromJson<ArrayList<MemberData>>(
                    json,
                    object : TypeToken<ArrayList<MemberData>>() {}.type
                )

                if (userList.isNullOrEmpty()){

                    onFireStoreCatchDataListener.onCatchDataFail()

                    return@addOnCompleteListener
                }

                var memberData = MemberData()

                AuthHandler.getCurrentUser()?.uid?.let{ uid ->
                    userList.forEach {
                        if (uid == it.userId){
                            memberData = it
                            return@forEach
                        }
                    }
                }
                if (memberData.email.isEmpty()){

                    onFireStoreCatchDataListener.onCatchDataFail()

                    return@addOnCompleteListener
                }
                onFireStoreCatchDataListener.onCatchDataSuccess(memberData)



            }



    }

    fun editMemberData(
        memberData: MemberData,
        onFireStoreCatchDataListener: OnFireStoreCatchDataListener<Unit>
    ) {
        firestore.collection(USER)
            .document(USER_LIST)
            .get()
            .addOnCompleteListener { task ->
                if (!task.isSuccessful && task.result == null){

                    onFireStoreCatchDataListener.onCatchDataFail()

                    return@addOnCompleteListener
                }
                val snapshot = task.result

                if (snapshot == null || snapshot.data == null){

                    onFireStoreCatchDataListener.onCatchDataFail()

                    return@addOnCompleteListener
                }


                val json = snapshot.data?.get("user_json") as String

                val userList = Gson().fromJson<ArrayList<MemberData>>(
                    json,
                    object : TypeToken<ArrayList<MemberData>>() {}.type
                )

                if (userList.isNullOrEmpty()){

                    onFireStoreCatchDataListener.onCatchDataFail()

                    return@addOnCompleteListener
                }

                AuthHandler.getCurrentUser()?.uid?.let{ uid ->
                    userList.forEach {
                        if (uid == it.userId){
                            it.description = memberData.description
                            it.name = memberData.name
                            return@forEach
                        }
                    }
                }

                val userJson = Gson().toJson(userList)
                saveUserListData(userJson,onFireStoreCatchDataListener)

            }
    }

    private fun saveUserListData(
        userJson: String,
        onFireStoreCatchDataListener: OnFireStoreCatchDataListener<Unit>
    ) {

        val map = HashMap<String,String>()

        map["user_json"] = userJson

        firestore.collection(USER)
            .document(USER_LIST)
            .set(map, SetOptions.merge())
            .addOnCompleteListener {
                if (it.isSuccessful){
                    onFireStoreCatchDataListener.onCatchDataSuccess(Unit)
                }else{
                    onFireStoreCatchDataListener.onCatchDataFail()
                }
            }
    }

    fun addEquipmentData(
        selectEquipmentArray: java.util.ArrayList<EquipmentData>,
        equipmentListTitle: String,
        onFireStoreCatchDataListener: OnFireStoreCatchDataListener<Unit>
    ) {

        val data = EquipmentUserData()

        data.name = equipmentListTitle
        data.selectTargetArray = selectEquipmentArray

        userSelectEquipmentArray.add(data)

        val json = Gson().toJson(userSelectEquipmentArray)


        val map = HashMap<String,String>()

        map["json"] = json

        AuthHandler.getCurrentUser()?.uid?.let{
            firestore.collection(USER_EQUIPMENT_LIST)
                .document(it)
                .set(map, SetOptions.merge())
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        onFireStoreCatchDataListener.onCatchDataSuccess(Unit)
                    }else{
                        onFireStoreCatchDataListener.onCatchDataFail()
                    }
                }
        }

    }

    fun getCurrentUserEquipmentData() {
        userSelectEquipmentArray = ArrayList()
        AuthHandler.getCurrentUser()?.uid?.let {
            firestore.collection(USER_EQUIPMENT_LIST)
                .document(it)
                .get()
                .addOnCompleteListener {task ->

                    if (!task.isSuccessful && task.result == null){

                        MichaelLog.i("沒有裝備資料 result is null")

                        return@addOnCompleteListener
                    }
                    val snapshot = task.result

                    if (snapshot == null || snapshot.data == null){

                        MichaelLog.i("沒有裝備資料 snapshot is null")

                        return@addOnCompleteListener
                    }


                    val json = snapshot.data?.get("json") as String

                    val equipmentList = Gson().fromJson<ArrayList<EquipmentUserData>>(
                        json,
                        object : TypeToken<ArrayList<EquipmentUserData>>() {}.type
                    )
                    if (equipmentList.isNullOrEmpty()){

                        MichaelLog.i("沒有裝備資料 data is null")

                        return@addOnCompleteListener
                    }
                    userSelectEquipmentArray.addAll(equipmentList)

                }
        }
    }

    fun getUserEquipmentData(onFireStoreCatchDataListener: OnFireStoreCatchDataListener<java.util.ArrayList<EquipmentUserData>>) {

        AuthHandler.getCurrentUser()?.uid?.let{

            val documentReference = firestore.collection(USER_EQUIPMENT_LIST).document(it)

            documentReference.addSnapshotListener { value, error ->

                if (error != null) {

                    onFireStoreCatchDataListener.onCatchDataFail()
                    MichaelLog.i("錯誤 無法取的使用者資料")
                    return@addSnapshotListener
                }

                if (value != null && value.exists()) {

                    val json = value.get("json") as String

                    if (json.isEmpty()) {
                        onFireStoreCatchDataListener.onCatchDataFail()
                        return@addSnapshotListener
                    }

                    val equipmentList = Gson().fromJson<ArrayList<EquipmentUserData>>(
                        json,
                        object : TypeToken<ArrayList<EquipmentUserData>>() {}.type
                    )
                    if (equipmentList.isNullOrEmpty()){
                        onFireStoreCatchDataListener.onCatchDataFail()
                        MichaelLog.i("沒有裝備資料 data is null")

                        return@addSnapshotListener
                    }

                    onFireStoreCatchDataListener.onCatchDataSuccess(equipmentList)

                }


            }




        }
    }

    fun deleteUserEquipmentData(
        userSelectDeleteData: java.util.ArrayList<EquipmentUserData>,
        onFireStoreCatchDataListener: OnFireStoreCatchDataListener<Unit>
    ) {

        getUserEquipmentList(object : OnFireStoreCatchDataListener<ArrayList<EquipmentUserData>>{
            override fun onCatchDataSuccess(data: ArrayList<EquipmentUserData>) {
                val iterator = data.iterator()

                while (iterator.hasNext()){

                    val data = iterator.next()

                    userSelectDeleteData.forEach {
                        if (data.equipmentID == it.equipmentID){
                            iterator.remove()
                            return@forEach
                        }
                    }
                }
                saveUserEquipmentData(data,onFireStoreCatchDataListener)
            }

            override fun onCatchDataFail() {
                onFireStoreCatchDataListener.onCatchDataFail()
            }
        })

    }

    private fun saveUserEquipmentData(
        equipmentList: java.util.ArrayList<EquipmentUserData>,
        onFireStoreCatchDataListener: OnFireStoreCatchDataListener<Unit>
    ) {

        val json = Gson().toJson(equipmentList)

        val map = HashMap<String,String>()

        map["json"] = json

        AuthHandler.getCurrentUser()?.uid?.let{

            firestore.collection(USER_EQUIPMENT_LIST)
                .document(it)
                .set(map, SetOptions.merge())
        }
        onFireStoreCatchDataListener.onCatchDataSuccess(Unit)

    }

    fun saveSingleUserEquipmentData(
        targetUserData: EquipmentUserData,
        onFireStoreCatchDataListener: OnFireStoreCatchDataListener<Unit>
    ) {
        getUserEquipmentList(object : OnFireStoreCatchDataListener<ArrayList<EquipmentUserData>>{
            override fun onCatchDataSuccess(data: ArrayList<EquipmentUserData>) {
                val dataArray = ArrayList<EquipmentUserData>(data)
                dataArray.forEach { equipment ->
                    if(equipment.equipmentID == targetUserData.equipmentID){
                        equipment.name = targetUserData.name
                        equipment.selectTargetArray = targetUserData.selectTargetArray
                        return@forEach
                    }
                }
                saveUserEquipmentData(dataArray,onFireStoreCatchDataListener)
            }

            override fun onCatchDataFail() {
                onFireStoreCatchDataListener.onCatchDataFail()
            }

        })
    }

    private fun getUserEquipmentList(onFireStoreCatchDataListener: OnFireStoreCatchDataListener<ArrayList<EquipmentUserData>>){

        AuthHandler.getCurrentUser()?.uid?.let {
            firestore.collection(USER_EQUIPMENT_LIST)
                .document(it)
                .get()
                .addOnCompleteListener {task ->
                    if (!task.isSuccessful && task.result == null){

                        MichaelLog.i("沒有裝備資料 result is null")
                        onFireStoreCatchDataListener.onCatchDataFail()
                        return@addOnCompleteListener
                    }
                    val snapshot = task.result

                    if (snapshot == null || snapshot.data == null){

                        MichaelLog.i("沒有裝備資料 snapshot is null")
                        onFireStoreCatchDataListener.onCatchDataFail()
                        return@addOnCompleteListener
                    }


                    val json = snapshot.data?.get("json") as String

                    val equipmentList = Gson().fromJson<ArrayList<EquipmentUserData>>(
                        json,
                        object : TypeToken<ArrayList<EquipmentUserData>>() {}.type
                    )
                    if (equipmentList.isNullOrEmpty()){

                        MichaelLog.i("沒有裝備資料 data is null")
                        onFireStoreCatchDataListener.onCatchDataFail()

                        return@addOnCompleteListener
                    }
                    onFireStoreCatchDataListener.onCatchDataSuccess(equipmentList)
                }
        }



    }


    interface OnCheckUserExistResultListener {
        fun onUserExist()
        fun onNeedToAddNewProfile()
        fun onFail()
    }

    interface OnFireStoreCatchDataListener<T> {
        fun onCatchDataSuccess(data: T)
        fun onCatchDataFail()
    }
}
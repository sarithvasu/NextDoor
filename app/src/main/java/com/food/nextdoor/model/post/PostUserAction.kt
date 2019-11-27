package com.food.nextdoor.model.post

import androidx.room.Entity
import androidx.room.PrimaryKey

class PostUserAction  {
    var userId: Int = -1
    var followUnfollow: FollowUnfollow = FollowUnfollow ()
    var shares: List<Share> = listOf()
    var productViews: List<ProductView> = listOf()
    var likeDislikeReviews: List<LikeDislike> = listOf()
    //val likeDislikeProducts: List<LikeDislikeProduct> = listOf()


    private constructor() {
        println("Instance Created")
    }

    companion object{
        val instance: PostUserAction by lazy {PostUserAction()}
    }
}

@Entity(tableName = "follow_unfollow")
class FollowUnfollow  {
    @PrimaryKey
    var followUnfollowId: Int = -1

    var sellerId: Int = -1
    var date: String = ""
    var time: String = ""
    var action: String =""
}

@Entity(tableName = "share")
class Share {
    @PrimaryKey(autoGenerate = true)
    var shareId: Int = 0
    var productId: Int = -1
    var sellerId: Int = -1
    var date: String = ""
    var time: String = ""
    var shareType: String = ""
}


@Entity(tableName = "product_view")
class ProductView {
    @PrimaryKey(autoGenerate = true)
    var productViewId: Int = 0
    var productId: Int = -1
    var sellerId: Int = -1
    var date: String = ""
    var startTime: String = ""
    var endTime: String? = null
}



@Entity(tableName = "like_dislike")
class LikeDislike (
    @PrimaryKey var likeDislikeId: Int = -1,
    var productId : Int,
    var sellerId : Int,
    var likeDislikeTypeId : Int,
    var date : String,
    var time : String,
    var opinion : String
)

//class LikeDislike (
//    val productId : Int,
//    val sellerId : Int,
//    val typeId : Int,
//    val date : String,
//    val time : String,
//    val type : String
//)
package com.food.nextdoor.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.food.nextdoor.model.*
import com.food.nextdoor.model.post.*

@Database(entities = [BuyerInfo::class, DeliveryType::class, PackingType::class, PaymentMode::class, ReviewTag::class,
    FollowUnfollow::class, Share::class, ProductView::class, LikeDislike::class,PostViewed::class,SharedDish::class],version = 1)

abstract class NextDoorDB :RoomDatabase() {
    abstract  val daoAccess :DaoAccess
    companion object{
        @Volatile private var instance:NextDoorDB ?= null
        private val LOCK=Any()
        operator fun invoke(context:Context)= instance ?: synchronized(LOCK){
            instance?: buildDB(context).also{ instance=it}
        }
        private fun buildDB(context: Context)=
            Room.databaseBuilder(context.applicationContext,NextDoorDB::class.java,"next_door.db").allowMainThreadQueries().fallbackToDestructiveMigration().build()
           // Room.databaseBuilder(context.applicationContext,NextDoorDB::class.java,"next_door.db").fallbackToDestructiveMigration().build()

    }
}
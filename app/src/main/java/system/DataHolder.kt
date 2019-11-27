package system

import com.food.nextdoor.model.HomeFeed

class DataHolder {

    init {
        println("Class init method.")
    }

    companion object
    {
        init {
            println("Singleton class invoked.")
        }

        private lateinit var m_homeFeedInstance: HomeFeed
        var homeFeedInstance: HomeFeed
            get() = this.m_homeFeedInstance
            set(value) {
                this.m_homeFeedInstance = value
            }
    }
}
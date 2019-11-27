package system

import com.food.nextdoor.model.BuyerInfo

class Manager {


    companion object {

        private lateinit var m_buyerInfo: BuyerInfo
        var buyerInfo: BuyerInfo
            get() = this.m_buyerInfo
            set(value) {
                this.m_buyerInfo = value
            }


        private var m_chefId: Int = 0
        var chefId: Int
            get() = this.m_chefId
            set(value) {
                this.m_chefId = value
            }



//        fun getReviewTagList(): ArrayList<ReviewTag> {
//            val reviewTagList: ArrayList<ReviewTag> = arrayListOf()
//            var reviewTag = ReviewTag()
//
//            reviewTag.reviewTagId = 1
//            reviewTag.tagName = "Awesome"
//            reviewTagList.add(reviewTag)
//
//            reviewTag = ReviewTag()
//            reviewTag.reviewTagId = 2
//            reviewTag.tagName = "Delight"
//            reviewTagList.add(reviewTag)
//
//            reviewTag = ReviewTag()
//            reviewTag.reviewTagId = 3
//            reviewTag.tagName = "Mouth watering"
//            reviewTagList.add(reviewTag)
//
//
//            reviewTag = ReviewTag()
//            reviewTag.reviewTagId = 4
//            reviewTag.tagName = "Yummy"
//            reviewTagList.add(reviewTag)
//
//
//            reviewTag = ReviewTag()
//            reviewTag.reviewTagId = 5
//            reviewTag.tagName = "Moderate"
//            reviewTagList.add(reviewTag)
//
//
//            reviewTag = ReviewTag()
//            reviewTag.reviewTagId = 6
//            reviewTag.tagName = "Toothsome"
//            reviewTagList.add(reviewTag)
//
//
//            reviewTag = ReviewTag()
//            reviewTag.reviewTagId = 7
//            reviewTag.tagName = "Delicious"
//            reviewTagList.add(reviewTag)
//
//
//            reviewTag = ReviewTag()
//            reviewTag.reviewTagId = 8
//            reviewTag.tagName = "Not bad"
//            reviewTagList.add(reviewTag)
//
//
//            reviewTag = ReviewTag()
//            reviewTag.reviewTagId = 9
//            reviewTag.tagName = "Not good"
//            reviewTagList.add(reviewTag)
//
//            return reviewTagList
//
//        }

        //Preference
//        class Preference {
//
////            fun getDeliveryTypeIdByDescription(deliveryDescription: String): Int {
////                var deliveryTypeId: Int = -1
////
////                val deliveryTypes: ArrayList<DeliveryType> = getDeliveryTypes()
////                val matchingDeliveryType = deliveryTypes.filter { d -> d.deliveryDescription == deliveryDescription }
////                if (matchingDeliveryType.isNotEmpty()) {
////                    deliveryTypeId = matchingDeliveryType[0].deliveryTypeId
////                } else {
////                    // Soumen need to adde xception
////                }
////                return deliveryTypeId
////            }
//
//
//
//
//
//
//
//
//
//            private fun getDeliveryTypes123(): ArrayList<DeliveryType> {
//                val deliveryTypes: ArrayList<DeliveryType> = arrayListOf()
//
//                val deliveryType = DeliveryType()
//                deliveryType.deliveryTypeId = 1
//                deliveryType.deliveryDescription = "Home delivery"
//                deliveryTypes.add(deliveryType)
//
//                val deliveryType1 = DeliveryType()
//                deliveryType1.deliveryTypeId = 2
//                deliveryType1.deliveryDescription = "Self pick"
//                deliveryTypes.add(deliveryType1)
//
//                return deliveryTypes
//            }
//
//
//
//
//
//            fun getDeliveryDescriptionById1234(deliveryTypeId: Int): String {
//                var description = ""
//
//                val deliveryTypes: ArrayList<DeliveryType> = getDeliveryTypes123()
//                val matchingDeliveryType: List<DeliveryType> =
//                    deliveryTypes.filter { d -> d.deliveryTypeId == deliveryTypeId }
//
//                if (matchingDeliveryType.isNotEmpty()) {
//                    description = matchingDeliveryType[0].deliveryDescription
//                } else {
//                    // Soumen need to adde xception
//                }
//                return description
//            }
//
//
//
//
//
//
//
//
//            fun getPackingTypeIdByDescription1234(packingDescription: String): Int {
//                var packingTypeId: Int = -1
//
//                val packingTypes: ArrayList<PackingType> = getPackingTypes1234()
//                val matchingPackingType: List<PackingType>
//                matchingPackingType = packingTypes.filter { d -> d.packingDescription == packingDescription }
//
//                if (matchingPackingType.isNotEmpty()) // Soumen need to adde xception
//                {
//                    packingTypeId = matchingPackingType[0].packingTypeId
//                } else {
//                    // Soumen need to adde xception
//                }
//                return packingTypeId
//            }
//
//
//
//
//            private fun getPackingTypes1234(): ArrayList<PackingType> {
//
//                val packingTypes: ArrayList<PackingType> = arrayListOf()
//
//                val packingType = PackingType()
//                packingType.packingTypeId = 1
//                packingType.packingDescription = "Get your own box"
//                packingTypes.add(packingType)
//
//                val packingType1 = PackingType()
//                packingType1.packingTypeId = 2
//                packingType1.packingDescription = "Parcel in disposable box"
//                packingTypes.add(packingType1)
//
//                return packingTypes
//            }
//
//
//
//
//
//            fun getPackingDescriptionById1234(packingTypeId: Int): String {
//                var description = ""
//
//                val packingTypes: ArrayList<PackingType> = getPackingTypes1234()
//                val matchingPackingType: List<PackingType> =
//                    packingTypes.filter { d -> d.packingTypeId == packingTypeId }
//
//                if (matchingPackingType.isNotEmpty()) {
//                    description = matchingPackingType[0].packingDescription
//                } else {
//                    // Soumen need to adde xception
//                }
//                return description
//            }
//        }







    }
}















package system

import com.food.nextdoor.model.BuyerInfo
import com.food.nextdoor.model.DeliveryType
import com.food.nextdoor.model.PackingType
import java.util.ArrayList

class Manager {


    companion object {

        private lateinit var m_buyerInfo: BuyerInfo
        var buyerInfo: BuyerInfo
            get() = this.m_buyerInfo
            set(value) {
                this.m_buyerInfo = value
            }

        class Preference () {

            fun getDeliveryTypeId(deliveryDescription: String) : Int {
                var deliveryTypeId: Int = -1


                var MatchingDeliveryTypes: ArrayList<DeliveryType> = getDeliveryTypes()
                if (MatchingDeliveryTypes.size > 0) {
                    deliveryTypeId =  MatchingDeliveryTypes[0].deliveryTypeId
                } else {
                    // Soumen need to adde xception
                }
                return deliveryTypeId
            }
            fun getDeliveryTypes(): ArrayList<DeliveryType> {
                var deliveryTypes: ArrayList<DeliveryType> = arrayListOf()

                var deliveryType = DeliveryType()
                deliveryType.deliveryTypeId = 1
                deliveryType.deliveryDescription ="Home Delivery"
                deliveryTypes.add(deliveryType)

                var deliveryType1 = DeliveryType()
                deliveryType1.deliveryTypeId = 2
                deliveryType1.deliveryDescription ="Self Pick"
                deliveryTypes.add(deliveryType1)

                return deliveryTypes
            }
            fun saveDeliveryType(deliveryType: DeliveryType) {
                // Soumen need to save it to prefereance
            }
            fun getDeliveryDescription(deliveryTypeId: Int) : String {
                var description: String = ""

                var deliveryTypes: ArrayList<DeliveryType> = getDeliveryTypes()
                var MatchingDeliveryType:  List<DeliveryType> =  deliveryTypes.filter { d-> d.deliveryTypeId == deliveryTypeId }

                if (MatchingDeliveryType.size > 0) {
                    description =  MatchingDeliveryType[0].deliveryDescription
                } else {
                    // Soumen need to adde xception
                }
                return description
            }


            fun getPackingTypeId(packingDescription: String) : Int {
               var packingTypeId: Int = -1

                var packingTypes: ArrayList<PackingType> = getPackingTypes()
                var MatchingPackingType:  List<PackingType> =  packingTypes.filter { d-> d.packingDescription == packingDescription }

                if (MatchingPackingType.size > 0) {
                    packingTypeId =  MatchingPackingType[0].packingTypeId
                } else {
                    // Soumen need to adde xception
                }
                return packingTypeId
            }
            fun getPackingTypes(): ArrayList<PackingType> {

                var packingTypes: ArrayList<PackingType> = arrayListOf()

                var packingType = PackingType()
                packingType.packingTypeId = 1
                packingType.packingDescription ="Get your box"
                packingTypes.add(packingType)

                var packingType1 = PackingType()
                packingType1.packingTypeId = 2
                packingType1.packingDescription ="Parcel in Disposable box"
                packingTypes.add(packingType1)

                return packingTypes
            }
            fun savePackingType(packingType: PackingType) {
                // Soumen need to save it to prefereance
            }
            fun getPackingDescription(packingTypeId: Int) : String {
                var description: String = ""

                var packingTypes: ArrayList<PackingType> = getPackingTypes()
                var MatchingPackingType:  List<PackingType> =  packingTypes.filter { d-> d.packingTypeId == packingTypeId }

                if (MatchingPackingType.size > 0) {
                    description =  MatchingPackingType[0].packingDescription
                } else {
                    // Soumen need to adde xception
                }
                return description
            }

        }
    }










    class  Dodoo() {
        fun myName(): String {
            return "My Name function called"
        }
    }
    fun add(): String {
        return "Add Function Called"
    }


}
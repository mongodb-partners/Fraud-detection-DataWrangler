package com.mongodb.app.domain

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Details() : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId.create()
    var isComplete: Boolean = false
    var card_number: String = ""
    var card_type: String = ""
    var trx_amount: String = ""
    var phone_number: String = ""
    var email_domin: String = ""
    var owner_id: String = ""

    constructor(ownerId: String = "") : this() {
        owner_id = ownerId
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is Details) return false
        if (this._id != other._id) return false
        if (this.isComplete != other.isComplete) return false
        if (this.card_number != other.card_number) return false
        if (this.card_type != other.card_type) return false
        if (this.owner_id != other.owner_id) return false
        return true
    }

    override fun hashCode(): Int {
        var result = _id.hashCode()
        result = 31 * result + isComplete.hashCode()
        result = 31 * result + card_number.hashCode()
        result = 31 * result + card_type.hashCode()
        result = 31 * result + owner_id.hashCode()
        return result
    }
}

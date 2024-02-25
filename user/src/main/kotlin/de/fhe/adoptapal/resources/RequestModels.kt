package de.fhe.adoptapal.resources

class CreateUserRequest {
    lateinit var username: String
    lateinit var email: String
    lateinit var password: String

    var phoneNumber: String? = null
    var addressId: Long? = null
}

class UpdateUserRequest {
    // TODO: move to header
    lateinit var password: String

    var newUsername: String? = null
    var newEmail: String? = null
    var newPassword: String? = null
    var newPhoneNumber: String? = null
    var newAddressId: Long? = null
}

class DeleteUserRequest {
    // TODO: move to header
    lateinit var password: String
}

class CreateAddressRequest {
    lateinit var street: String
    lateinit var city: String
    lateinit var postalCode: String
}

class UpdateAddressRequest {
    var newStreet: String? = null
    var newCity: String? = null
    var newPostalCode: String? = null
}

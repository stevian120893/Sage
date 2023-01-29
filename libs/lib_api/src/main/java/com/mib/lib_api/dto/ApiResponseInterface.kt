package com.mib.lib_api.dto

interface ApiResponseInterface<out S : Any> {
    /**
     * The raw response to get error objects
     *
     * {
     *     "errors": [
     *         {
     *             "message": "Validation failed for field 'OTPID' on the rule 'required'",
     *             "code": 400,
     *             "field": "OTPID"
     *         }
     *     ]
     * }
     */
    val rawData: String?
}
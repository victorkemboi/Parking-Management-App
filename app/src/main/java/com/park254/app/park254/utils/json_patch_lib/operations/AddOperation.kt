package com.park254.app.park254.utils.json_patch_lib.operations


import com.google.gson.annotations.Expose
import com.park254.app.park254.utils.json_patch_lib.JsonPatchOperation

class AddOperation(@Expose val path: String, @Expose val value: Any) : JsonPatchOperation("add")

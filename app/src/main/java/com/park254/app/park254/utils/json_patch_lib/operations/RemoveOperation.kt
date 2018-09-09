package com.park254.app.park254.utils.json_patch_lib.operations

import com.google.gson.annotations.Expose
import com.park254.app.park254.utils.json_patch_lib.JsonPatchOperation

class RemoveOperation(@Expose val path: String) : JsonPatchOperation("remove")

package com.park254.app.park254.utils.json_patch_lib

import com.google.gson.annotations.Expose

abstract class JsonPatchOperation(@Expose val op: String)

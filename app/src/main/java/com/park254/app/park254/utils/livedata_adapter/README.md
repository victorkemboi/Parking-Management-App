# LiveData Adapter for Retrofit

Copy the following classes in your project to use `LiveData<ApiResponse<T>>` instead of `Call<T>` in Retrofit services.
Made this gist so everyone can just copy and paste them in project rather than finding through the Google Samples.

Original Credits: I didn't make these classes, they are from [Google Sample](https://github.com/googlesamples/android-architecture-components/tree/master/GithubBrowserSample) 

## Difference

### Traditional way - with `Call`:

```
interface SomeService {

    @GET("data")
    fun getData(): Call<T>
}
```

### Traditional way - with `LiveData`:

```
interface SomeService {

    @GET("data")
    fun getData(): LiveData<ApiResponse<T>>
}
```

## Usage

Set the `LiveDataCallAdapterFactory` in the Retrofit Builder

```
Retrofit.Builder()
        .baseUrl(BASE_URL)
        // ....
        .addCallAdapterFactory(LiveDataCallAdapterFactory())
        // ....
        .build()
```                   
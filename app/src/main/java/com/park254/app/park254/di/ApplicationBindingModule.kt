
import com.park254.app.park254.ui.LoginActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
interface ApplicationBindingModule {

    @ContributesAndroidInjector
    fun loginActivity(): LoginActivity
}
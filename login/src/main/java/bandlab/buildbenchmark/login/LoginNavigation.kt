package bandlab.buildbenchmark.login

import bandlab.buildbenchmark.AppScope
import bandlab.buildbenchmark.CustomAnnotation
import bandlab.buildbenchmark.login.api.LoginNavigation
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class LoginNavigationImpl @Inject constructor() : LoginNavigation {
    override fun navigateToLogin() {
        println("Navigate to logina")
        println("Navigate to logina")
    }
}

@CustomAnnotation
interface LoginLogger

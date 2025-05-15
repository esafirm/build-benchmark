package bandlab.buildbenchmark

import com.squareup.anvil.annotations.ContributesBinding
import com.squareup.anvil.annotations.MergeComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@MergeComponent(AppScope::class)
interface AppComponent

interface Authenticator

@ContributesBinding(AppScope::class)
class RealAuthenticator @Inject constructor() : Authenticator

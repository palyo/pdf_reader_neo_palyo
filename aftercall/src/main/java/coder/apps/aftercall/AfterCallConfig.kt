package coder.apps.aftercall

import android.content.Context
import android.content.Intent

/**
 * Public configuration surface for the after-call module.
 *
 * Host apps initialise this once at startup (Application.onCreate) and the
 * module reads from it at runtime — keeps integration to a single function
 * call regardless of which app is hosting the module.
 */
object AfterCallConfig {

    /**
     * The component the user lands on after tapping the "Open App" icon
     * inside the post-call screen. If null, that button is a no-op.
     */
    @Volatile
    var hostLauncherComponent: Class<*>? = null
        private set

    /**
     * Optional supplier override — set this when you need to launch with
     * extras / flags / actions that change at runtime. Takes precedence
     * over [hostLauncherComponent] when both are set.
     */
    @Volatile
    var hostLauncherIntentFactory: ((Context) -> Intent)? = null
        private set

    /** Production AdMob banner unit IDs (primary, fallback). */
    @Volatile var bannerAdUnitId1: String? = null
        private set
    @Volatile var bannerAdUnitId2: String? = null
        private set

    /** Production AdMob native unit ID. */
    @Volatile var nativeAdUnitId: String? = null
        private set

    /** When true, the receivers/services skip launching the post-call screen. */
    @Volatile var disabled: Boolean = false

    /**
     * Optional primary action card (the big "Voice Recording" tile shown
     * above the tool grid). Tapping it launches [primaryAction.intentFactory].
     * If null, the card is hidden.
     */
    @Volatile var primaryAction: AfterCallTool? = null
        private set

    /**
     * Tools shown in the RecyclerView grid below the primary card. Each
     * tool is a label + icon + intent factory. Order is preserved.
     */
    private val _tools: MutableList<AfterCallTool> = mutableListOf()
    val tools: List<AfterCallTool> get() = _tools.toList()

    fun setHostLauncher(activityClass: Class<*>) {
        hostLauncherComponent = activityClass
        hostLauncherIntentFactory = null
    }

    fun setHostLauncher(factory: (Context) -> Intent) {
        hostLauncherIntentFactory = factory
    }

    fun setAdUnits(banner1: String? = null, banner2: String? = null, nativeId: String? = null) {
        bannerAdUnitId1 = banner1
        bannerAdUnitId2 = banner2
        nativeAdUnitId = nativeId
    }

    /** Set the big "primary action" tile shown above the tool grid. */
    fun setPrimaryAction(tool: AfterCallTool?) {
        primaryAction = tool
    }

    /** Replace the tool list shown in the grid. Order is preserved. */
    fun setTools(tools: List<AfterCallTool>) {
        _tools.clear()
        _tools.addAll(tools)
    }

    /** Append a tool to the grid. */
    fun addTool(tool: AfterCallTool) {
        _tools.add(tool)
    }

    /** Remove all registered tools. */
    fun clearTools() {
        _tools.clear()
    }

    internal fun buildHostLaunchIntent(context: Context): Intent? {
        hostLauncherIntentFactory?.invoke(context)?.let { return it }
        val cls = hostLauncherComponent ?: return null
        return Intent(context, cls).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
    }
}

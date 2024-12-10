package com.evervault.sampleapplication
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.hamcrest.Matchers.notNullValue
import org.junit.Before

private const val BASIC_SAMPLE_PACKAGE = "com.evervault.sampleapplication"
private const val LAUNCH_TIMEOUT = 5000L

@RunWith(AndroidJUnit4::class)
class AttestationTest {

    private lateinit var device: UiDevice

    @Before
    fun startMainActivityFromHomeScreen() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        device.pressHome()

        val launcherPackage: String = device.launcherPackageName
        assertThat(launcherPackage, notNullValue())
        device.wait(
            Until.hasObject(By.pkg(launcherPackage).depth(0)),
            LAUNCH_TIMEOUT
        )

        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(
            BASIC_SAMPLE_PACKAGE)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)

        device.wait(
            Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
            LAUNCH_TIMEOUT
        )
    }

    @Test
    fun debugComposeViewHierarchy() {
        device.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE)), LAUNCH_TIMEOUT)

        val scrollable = UiScrollable(UiSelector().scrollable(true))
        scrollable.setAsVerticalList()
        val buttonSelector = UiSelector().description("Enclave Button")
        scrollable.scrollIntoView(buttonSelector)

        device.wait(Until.hasObject(By.res("Enclave Button")), LAUNCH_TIMEOUT)

        val enclaveButton = device.findObject(By.res("Enclave Button"))
        assertThat("Enclave Button not found", enclaveButton, notNullValue())
        enclaveButton.click()

        Thread.sleep(4000)

        device.wait(Until.hasObject(By.res("Enclave Response")), LAUNCH_TIMEOUT)

        val enclaveResponse = device.findObject(By.res("Enclave Response"))
        assertNotNull(enclaveResponse.text)
    }
}
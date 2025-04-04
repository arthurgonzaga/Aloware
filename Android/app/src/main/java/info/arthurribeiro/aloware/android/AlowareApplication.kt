package info.arthurribeiro.aloware.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Looper
import info.arthurribeiro.aloware.android.services.VoiceService
import info.arthurribeiro.aloware.android.utils.Constants

@SuppressLint("StaticFieldLeak")
class AlowareApplication : Application() {
    private var serviceConnectionManager: ServiceConnectionManager? = null

    companion object {
        private var instance: AlowareApplication? = null

        @JvmStatic
        fun voiceService(task: VoiceServiceTask) {
            instance?.serviceConnectionManager?.invoke(task)
        }
    }

    fun interface VoiceServiceTask {
        fun run(voiceService: VoiceService?)
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        // bind to voice service to keep it active
        serviceConnectionManager = ServiceConnectionManager(this, BuildConfig.ACCESS_TOKEN)
    }

    override fun onTerminate() {
        // Note: this method is not called when running on device, devices just kill the process.
        serviceConnectionManager!!.unbind()

        super.onTerminate()
    }

    private class ServiceConnectionManager(
        private val context: Context,
        private val accessToken: String,
    ) {
        private var voiceService: VoiceService? = null
        private val pendingTasks: MutableList<VoiceServiceTask> = ArrayList()
        private val serviceConnection: ServiceConnection =
            object : ServiceConnection {
                override fun onServiceConnected(
                    name: ComponentName,
                    service: IBinder,
                ) {
                    // verify is main thread, all Voice SDK calls must be made on the same Looper thread
                    assert(Looper.myLooper() == Looper.getMainLooper())
                    // link to voice service
                    voiceService = (service as VoiceService.VideoServiceBinder).service
                    // run tasks
                    synchronized(this@ServiceConnectionManager) {
                        for (task in pendingTasks) {
                            task.run(voiceService)
                        }
                        pendingTasks.clear()
                    }
                }

                override fun onServiceDisconnected(name: ComponentName) {
                    voiceService = null
                }
            }

        fun unbind() {
            if (null != voiceService) {
                context.unbindService(serviceConnection)
            }
        }

        fun invoke(task: VoiceServiceTask) {
            if (null != voiceService) {
                // verify is main thread, all Voice SDK calls must be made on the same Looper thread
                assert(Looper.myLooper() == Looper.getMainLooper())
                // run task
                synchronized(this) {
                    task.run(voiceService)
                }
            } else {
                // queue runnable
                pendingTasks.add(task)
                // bind to service
                val intent = Intent(context, VoiceService::class.java)
                intent.putExtra(Constants.ACCESS_TOKEN, accessToken)
                intent.putExtra(Constants.CUSTOM_RINGBACK, false)
                context.bindService(
                    intent,
                    serviceConnection,
                    BIND_AUTO_CREATE,
                )
            }
        }
    }
}

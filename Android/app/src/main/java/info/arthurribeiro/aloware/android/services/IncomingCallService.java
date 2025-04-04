package info.arthurribeiro.aloware.android.services;

import static java.lang.String.format;

import static info.arthurribeiro.aloware.android.utils.Constants.ACTION_CANCEL_CALL;
import static info.arthurribeiro.aloware.android.utils.Constants.ACTION_INCOMING_CALL;
import static info.arthurribeiro.aloware.android.utils.Constants.CANCELLED_CALL_INVITE;
import static info.arthurribeiro.aloware.android.utils.Constants.INCOMING_CALL_INVITE;

import android.content.Intent;
import android.os.Parcelable;
import android.util.Pair;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.twilio.voice.CallException;
import com.twilio.voice.CallInvite;
import com.twilio.voice.CancelledCallInvite;
import com.twilio.voice.MessageListener;
import com.twilio.voice.Voice;

import info.arthurribeiro.aloware.android.utils.Logger;

public class IncomingCallService extends FirebaseMessagingService implements MessageListener {
    private static final Logger log = new Logger(IncomingCallService.class);

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        log.debug(format(
                "Received firebase message\n\tmessage data: %s\n\tfrom: %s",
                remoteMessage.getData(),
                remoteMessage.getFrom()));

        // Check if message contains a data payload.
        if (!remoteMessage.getData().isEmpty() && !Voice.handleMessage(this, remoteMessage.getData(), this)) {
            log.error(format("Received message was not a valid Twilio Voice SDK payload: %s", remoteMessage.getData()));
        }
    }

    @CallSuper
    @Override
    public void onNewToken(@NonNull String token) {
        log.debug("[debug] onNewToken");
    }

    @Override
    public void onCallInvite(@NonNull CallInvite callInvite) {
        startVoiceService(
                ACTION_INCOMING_CALL,
                new Pair<>(INCOMING_CALL_INVITE, callInvite));
    }

    @Override
    public void onCancelledCallInvite(@NonNull CancelledCallInvite cancelledCallInvite,
                                      @Nullable CallException callException) {
        startVoiceService(
                ACTION_CANCEL_CALL,
                new Pair<>(CANCELLED_CALL_INVITE, cancelledCallInvite));
    }

    @SafeVarargs
    private void startVoiceService(@NonNull final String action,
                                   @NonNull final Pair<String, Object>...data) {
        final Intent intent = new Intent(this, VoiceService.class);
        intent.setAction(action);
        for (Pair<String, Object> pair: data) {
            if (pair.second instanceof String) {
                intent.putExtra(pair.first, (String)pair.second);
            } else if (pair.second instanceof Parcelable) {
                intent.putExtra(pair.first, (Parcelable) pair.second);
            }
        }
        startService(intent);
    }
}
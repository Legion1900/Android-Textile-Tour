package com.legion1900.textiletour

import android.util.Log
import io.textile.pb.Model
import io.textile.textile.FeedItemData
import io.textile.textile.TextileEventListener

class MyTextileEventListener : TextileEventListener {
    override fun threadRemoved(threadId: String?) {
        log("threadRemoved")
    }

    override fun accountPeerAdded(peerId: String?) {
        log("accountPeerAdded(peerId=$peerId)")
    }

    override fun nodeStarted() {
        log("nodeStarted()")
    }

    override fun clientThreadQueryResult(queryId: String?, thread: Model.Thread?) {
        log("clientThreadQueryResult")
    }

    override fun willStopNodeInBackgroundAfterDelay(seconds: Int) {
        log("willStopNodeInBackgroundAfterDelay")
    }

    override fun syncFailed(status: Model.CafeSyncGroupStatus?) {
        log("syncFailed")
    }

    override fun queryDone(queryId: String?) {
        log("queryDone")
    }

    override fun threadAdded(threadId: String?) {
        log("threadAdded")
    }

    override fun syncUpdate(status: Model.CafeSyncGroupStatus?) {
        log("syncUpdate")
    }

    override fun nodeFailedToStop(e: Exception?) {
        log("nodeFailedToStop", e)
    }

    override fun queryError(queryId: String?, e: Exception?) {
        log("queryError")
    }

    override fun nodeOnline() {
        log("nodeOnline")
    }

    override fun notificationReceived(notification: Model.Notification?) {
        log("notificationReceived")
    }

    override fun accountPeerRemoved(peerId: String?) {
        log("accountPeerRemoved(peerId=$peerId)")
    }

    override fun contactQueryResult(queryId: String?, contact: Model.Contact?) {
        log("contactQueryResult")
    }

    override fun nodeFailedToStart(e: Exception?) {
        log("nodeFailedToStart", e)
    }

    override fun canceledPendingNodeStop() {
        log("canceledPendingNodeStop")
    }

    override fun syncComplete(status: Model.CafeSyncGroupStatus?) {
        log("syncComplete")
    }

    override fun nodeStopped() {
        log("nodeStopped")
    }

    override fun threadUpdateReceived(threadId: String?, feedItemData: FeedItemData?) {
        log("threadUpdateReceived")
    }

    private fun log(msg: String, e: Exception? = null) {
        e?.let { Log.d(TAG, msg, e) }
        Log.d(TAG, msg)
    }

    companion object {
        const val TAG = "test"
    }
}
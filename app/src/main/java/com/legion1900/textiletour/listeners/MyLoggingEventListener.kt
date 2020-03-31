package com.legion1900.textiletour.listeners

import android.util.Log
import io.textile.pb.Model
import io.textile.pb.View
import io.textile.textile.TextileEventListener

class MyLoggingEventListener : TextileEventListener {
    override fun threadRemoved(threadId: String?) {
        log("threadRemoved id: $threadId")
    }

    override fun accountPeerAdded(peerId: String?) {
        log("accountPeerAdded(peerId=$peerId)")
    }

    override fun nodeStarted() {
        log("nodeStarted")
    }

    override fun clientThreadQueryResult(queryId: String?, thread: Model.Thread?) {
        log("clientThreadQueryResult: $queryId, thread: $thread")
    }

    override fun willStopNodeInBackgroundAfterDelay(seconds: Int) {
        log("willStopNodeInBackgroundAfterDelay seconds: $seconds")
    }

    override fun queryDone(queryId: String?) {
        log("queryDone id: $queryId")
    }

    override fun threadAdded(threadId: String?) {
        log("threadAdded id: $threadId")
    }

    override fun nodeFailedToStop(e: Exception?) {
        log("nodeFailedToStop", e)
    }

    override fun queryError(queryId: String?, e: Exception?) {
        log("queryError id: $queryId", e)
    }

    override fun nodeOnline() {
        log("nodeOnline on thread: ${Thread.currentThread()}")
    }

    override fun notificationReceived(notification: Model.Notification?) {
        log("notificationReceived notification: $notification")
    }

    override fun accountPeerRemoved(peerId: String?) {
        log("accountPeerRemoved(peerId=$peerId)")
    }

    override fun contactQueryResult(queryId: String?, contact: Model.Contact?) {
        log(contact.toString())
    }

    override fun nodeFailedToStart(e: Exception?) {
        log("nodeFailedToStart", e)
    }

    override fun canceledPendingNodeStop() {
        log("canceledPendingNodeStop")
    }

    override fun nodeStopped() {
        log("nodeStopped")
    }

    override fun threadUpdateReceived(feedItem: View.FeedItem?) {
        TODO("Not yet implemented")
    }


    private fun log(msg: String, e: Exception? = null) {
        val newMsg = "listener name:${toString()} $msg"
        e?.let { Log.e(TAG, newMsg, e) }
        Log.d(TAG, newMsg)
    }

    companion object {
        const val TAG = "test"
    }
}

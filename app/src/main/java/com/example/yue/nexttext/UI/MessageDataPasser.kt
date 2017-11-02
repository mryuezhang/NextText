package com.example.yue.nexttext.UI

import com.example.yue.nexttext.DataType.MessageWrapper

/**
 * Created by yue on 2017-11-01.
 */
interface MessageDataPasser {
    fun onDataPass(data: MessageWrapper)
}
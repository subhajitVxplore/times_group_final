package com.vxplore.thetimesgroup.extensions

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class MobileNumberValidator : VisualTransformation {
    private val maxLength = MOBILE_NUMBER_PATTERN.count { it == MASK_CHAR }

    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.length > maxLength) text.take(maxLength) else text

        val annotatedString = buildAnnotatedString {
            if (trimmed.isEmpty()) return@buildAnnotatedString

            var maskIndex = 0
            var textIndex = 0
            while (textIndex < trimmed.length && maskIndex < MOBILE_NUMBER_PATTERN.length) {
                if (MOBILE_NUMBER_PATTERN[maskIndex] != MASK_CHAR) {
                    val nextDigitIndex = MOBILE_NUMBER_PATTERN.indexOf(MASK_CHAR, maskIndex)
                    append(MOBILE_NUMBER_PATTERN.substring(maskIndex, nextDigitIndex))
                    maskIndex = nextDigitIndex
                }
                append(trimmed[textIndex++])
                maskIndex++
            }
        }

        return TransformedText(annotatedString, PhoneOffsetMapper(MOBILE_NUMBER_PATTERN, MASK_CHAR))
    }

    companion object {
        const val MOBILE_NUMBER_PATTERN = "000 000 0000"
        val MASK_CHAR: Char = MOBILE_NUMBER_PATTERN.first()
    }


    inner class PhoneOffsetMapper(private val mask: String, private val numberChar: Char) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            var noneDigitCount = 0
            var i = 0
            while (i < offset + noneDigitCount) {
                if (mask[i++] != numberChar) noneDigitCount++
            }
            return offset + noneDigitCount
        }

        override fun transformedToOriginal(offset: Int): Int =
            offset - mask.take(offset).count { it != numberChar }
    }
}


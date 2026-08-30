package com.miozira.presentation.child

fun nextContentIndex(currentIndex: Int, size: Int): Int =
    (currentIndex + 1) % size

fun previousContentIndex(currentIndex: Int, size: Int): Int =
    (currentIndex - 1 + size) % size

fun isReviewNavigationEnabled(isPlaying: Boolean): Boolean = !isPlaying

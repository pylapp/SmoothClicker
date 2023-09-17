# Use "adb shell getevent" to get the good event channel
# Use "adb shell input keyevent 26" to press the POWER button
# See http://stackoverflow.com/questions/23529460/is-there-a-way-to-unlock-android-phone-via-adb-if-i-know-the-pattern
# Samsung Galaxy A3 with a 3x3 grid pattern, and schema: 7 - 5 - 8 - 9

sendevent /dev/input/event3 3 57 14

sendevent /dev/input/event3 1 330 1

sendevent /dev/input/event3 3 53 136
sendevent /dev/input/event3 3 54 773
sendevent /dev/input/event3 3 58 57
sendevent /dev/input/event3 0 0 0

sendevent /dev/input/event3 3 53 288
sendevent /dev/input/event3 3 54 620
sendevent /dev/input/event3 3 58 57
sendevent /dev/input/event3 0 0 0

sleep 1

sendevent /dev/input/event3 3 53 297
sendevent /dev/input/event3 3 54 763
sendevent /dev/input/event3 3 58 57
sendevent /dev/input/event3 0 0 0

sleep 1

sendevent /dev/input/event3 3 53 437
sendevent /dev/input/event3 3 54 758
sendevent /dev/input/event3 3 58 57
sendevent /dev/input/event3 0 0 0

sleep 1

sendevent /dev/input/event3 3 57 4294967295
sendevent /dev/input/event3 1 330 0
sendevent /dev/input/event3 0 0 0

echo "UNLOCK_DONE" # Do not forget it!

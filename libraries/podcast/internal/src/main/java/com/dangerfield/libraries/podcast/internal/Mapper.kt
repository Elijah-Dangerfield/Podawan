//package com.dangerfield.libraries.podcast.internal
//
//import android.os.Bundle
//
//interface DirectMapper<FROM, TO> {
//    fun map(from: FROM): TO
//
//}
//
//interface IndirectMapper<FROM, TO, ARG> {
//    fun map(from: FROM, arg: ARG): TO
//
//}
//
//interface SafeDirectMapper<FROM, TO> {
//    fun map(from: FROM): Try<TO>
//
//}
//
//interface SafeIndirectMapper<FROM, TO, ARG> {
//    fun map(from: FROM, arg: ARG):Try<TO>
//
//}
//
////////////////////////////////////////////////
//// Alternative
//
//interface Mapper<FROM, TO> {
//    fun map(from: FROM, args: Bundle = Bundle()): TO
//}
//
//
//class MyClass: Mapper<FROM, TO> {
//    override fun map(from: FROM, args: Bundle): TO {
//        // implementation
//
//    }
//}
//
//interface SafeMapper<FROM, TO> {
//    fun map(from: FROM, args: Bundle = Bundle()): Try<TO>
//}
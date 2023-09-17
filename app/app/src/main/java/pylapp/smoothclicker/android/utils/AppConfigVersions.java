/*
    MIT License

    Copyright (c) 2016  Pierre-Yves Lapersonne (Mail: dev@pylapersonne.info)

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
 */
// ✿✿✿✿ ʕ •ᴥ•ʔ/ ︻デ═一

package pylapp.smoothclicker.android.utils;

/**
 * Just a configuration class for versions
 *
 * A version of an app should be found by three things : the version code, the version name and the version tag.
 * The version code is incremented each time a new version is released.
 * The version name is the "user-friendly" version, i.e. with a schema like XX.YY.ZZ.
 * The XX is incremented for very important upgrades, major updates.
 * The YY is incremented or common upgrades, medium.
 * The ZZ is incremented for minor upgrades, i.e. updates for refactoring or cleaning facts.
 * The version tag is a code name, easy to remember. It should be changed each time the XX or YY part is modified.
 *
 * @author Pierre-Yves Lapersonne
 * @version 1.6.1
 * @since 16/03/2016
 */
public final class AppConfigVersions {


    /**
     * Version tag for v1.0.x
     */
    public static final String VERSION_TAG_1_0_0 = "Astonishing Ant";
    /**
     * Version tag for v1.3.x
     */
    public static final String VERSION_TAG_1_3_0 = "Blazing Buffalo";
    /**
     * Version tag for v1.4.x
     */
    public static final String VERSION_TAG_1_4_0 = "Crazy Crane";
    /**
     * Version tag for v1.5.x
     */
    public static final String VERSION_TAG_1_5_0 = "Dumb Dodo";
    /**
     * Version tag for v1.6.x
     */
    public static final String VERSION_TAG_1_6_0 = "Elastic Elephant";
    /**
     * Version tag for v1.7.x
     */
    public static final String VERSION_TAG_1_7_0 = "Freaky Fawn";
    /**
     * Version tag for v1.8.x
     */
    public static final String VERSION_TAG_1_8_0 = "Galactic Gorilla";
    /**
     * Version tag for v1.9.x
     */
    public static final String VERSION_TAG_1_9_0 = "Holy Hedgehog";
    /**
     * Version tag for v2.0.x
     */
    public static final String VERSION_TAG_2_0_0 = "Incredible Indri";
    /**
     * Version tag for v2.1.x
     */
    public static final String VERSION_TAG_2_1_0 = "Juicy Jellyfish";

    /*
     * The current version tag
     */
    public static final String VERSION_TAG_CURRENT = VERSION_TAG_2_1_0;

}

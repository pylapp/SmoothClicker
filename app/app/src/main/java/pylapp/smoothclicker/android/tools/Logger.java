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

package pylapp.smoothclicker.android.tools;

import android.util.Log;

/**
 * Class to use to log things with a small amount of fun.
 * 
 * @author Pierre-Yves Lapersonne
 * @version 2.0.0
 * @since 02/03/2016
 */
@SuppressWarnings("unused")
public final class Logger {

	/**
	 * Logs the message with the dedicated tag
	 * 
	 * @param logLevel - The log level
	 * @param logTag - The log tag
	 * @param message - The message to log
	 */
	public static void log( LogLevel logLevel, String logTag, String message ){
		switch ( logLevel ){
			case VERBOSE:
				Log.v(logTag, message);
				break;
			case INFORMATION:
				Log.i(logTag, message);
				break;
			case DEBUG:
				Log.d(logTag, message);
				break;
			case WARNING:
				Log.w(logTag, message);
				break;
			case ERROR:
				Log.e(logTag, message);
				break;
			case FATAL_ERROR:
				Log.wtf(logTag, createDalek());
				Log.wtf(logTag, message);
				break;
			case WTF_ERROR:
				Log.wtf(logTag, createReaper());
				Log.wtf(logTag, message);
				break;
		}
	}
	
	/**
	 * Logs the message with the "debug" stream
	 * @param logTag - The log tag
	 * @param message - The message
	 */
	public static void d( String logTag, String message ){
		log( LogLevel.DEBUG, logTag, message );
	}

	/**
	 * Logs the message with the "info" stream
	 * @param logTag - The log tag
	 * @param message - The message
	 */
	public static void i( String logTag, String message ){
		log( LogLevel.INFORMATION, logTag, message );
	}

	/**
	 * Logs the message with the "verbose" stream
	 * @param logTag - The log tag
	 * @param message - The message
	 */
	public static void v( String logTag, String message ){
		log( LogLevel.VERBOSE, logTag, message );
	}

	/**
	 * Logs the message with the "warning" stream
	 * @param logTag - The log tag
	 * @param message - The message
	 */
	public static void w( String logTag, String message ){
		log( LogLevel.WARNING, logTag, message );
	}

	/**
	 * Logs the message with the "error" stream
	 * @param logTag - The log tag
	 * @param message - The message
	 */
	public static void e( String logTag, String message ){
		log( LogLevel.ERROR, logTag, message );
	}

	/**
	 * Logs the message with the "fatal error" stream
	 * @param logTag - The log tag
	 * @param message - The message
	 */
	public static void fe( String logTag, String message ){
		log( LogLevel.FATAL_ERROR, logTag, message );
	}

	/**
	 * Logs the message with the "wtf" stream
	 * @param logTag - The log tag
	 * @param message - The message
	 */
    public static void wtf( String logTag, String message ){
		log( LogLevel.WTF_ERROR, logTag, message );
	}
	
	/**
	 * Creates a rosa
	 * @return String - The ASCII-art rosa
	 */
	private static String createRosa(){
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append("          __ \n");
		sb.append("     _   / /| \n");
		sb.append("    |\\\\  \\/_/ \n");
		sb.append("    \\_\\| / __ \n");
		sb.append("       \\/_/__\\           .-=='/~\\ \n");
		sb.append("____,__/__,_____,______)/   /{~}}} \n");
		sb.append("-,------,----,-----,---,\\'-' {{~}} \n");
		sb.append("                         '-==.\\}/ \n");
		return sb.toString();
	}
	
	/**
	 *  Displays in the logcat a rosa
	 * @param lvl - The level of display, related to the color use in the console
	 */
	public static void displayRosa(LogLevel lvl){
		log(lvl, "Rose", createRosa());
	}
	
	/**
	 * Creates a dalek
	 * @return String - The ASCII-art dalek
	 */
	private static String createDalek(){
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append("     _n____n__\n");
		sb.append("     /         \\---||--<\n");
		sb.append("    /___________\\\n");
		sb.append("    _|____|____|_\n");
		sb.append("    _|____|____|_\n");
		sb.append("     |    |    |\n");
		sb.append("    --------------\n");
		sb.append("    | || || || ||\\\n");
		sb.append("    | || || || || \\++++++++------<      *pew*  *pew*\n");
		sb.append("    ===============\n");
		sb.append("    |   |  |  |   |\n");
		sb.append("   (| O | O| O| O |)\n");
		sb.append("   |   |   |   |   |\n");
		sb.append("  (| O | O | O | O |)\n");
		sb.append("   |   |   |   |    |\n");
		sb.append(" (| O |  O | O  | O |)\n");
		sb.append("  |   |    |    |    |\n");
		sb.append(" (| O |  O |  O |  O |)\n");
		sb.append(" ======================\n");
		return sb.toString();
	}
	
	/**
	 * Displays a dalek in the logcat
	 * @param lvl - The level of display, related to the color use in the console
	 */
	public static void displayDalek( LogLevel lvl ){
		log(lvl, "Exterminate", createDalek());
	}
	
	/**
	 * Displays a reaper
	 * @return String - The ASCII-art reaper
	 */
	private static String createReaper(){
		
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n\ntttttttttttttttt11ttttttttttttttttttttttL88888888C888888L1tt1tttttttttttttttttttttttt1111111111111tttt\n");
		sb1.append("tttttttttttt1tttttttttttttttttttttttttttG88888888C8888880ttttttttttttttttttttttttttttt11111111t1111ttt\n");
		sb1.append("ttttttttttttt1tttttttttttttttttttttttttf888888888C8888880Ltttttttttttttttttttttttttttttttttttttttttttt\n");
		sb1.append("ttttttttttt111tttttttttttttttttttttttttG888888888888888800tttttttttttttttttttttttttttttttttttttt11111t\n");
		sb1.append("tttttttt1111111tttttttttttttttttttttttf0088888888888888800Ctttttttttttttttttttttttttttttttttttt11111tt\n");
		sb1.append("ttt11111111111111111111111ttttttttttttG88888888880888888888fttttttttttttttttttttttttttttttttttt111tttt\n");
		sb1.append("tt1111111111111111111111111111tttttttt808888888888888888880Gtttttttttttttttttttttttttttttttttttttttttt\n");
		sb1.append("t111111111111111111111111111111ttttttG0088888888888888888000Cttttttttttttttttttttt1t1ttttttttttttttttt\n");
		sb1.append("111111111111111111ttttt1111111ttttttt808888888888888888888000tttttttttttttttttt11111111111111111tttttf\n");
		sb1.append("111111111111111111111111t111tttttttf8088888888888888888888880Gt1tttttttttt1111111111111111111111tttttt\n");
		sb1.append("iii111111111111111111111111tttttttt000888888888888888888000000Li1111tt1111111111111111111111111111tttt\n");
		sb1.append("iiiiiii111111111111111111111tttt1t80888888888888888888888800000Ct1111111111111111111111111111111111111\n");
		sb1.append("iiiiiiiii111111111iiiiiiiii111111G888888888888888888888888800000G;iii1111111111111111111111111111iiiii\n");
		sb1.append("iii;;;;iiiii111iii;;;iiiiiiiiiiit88888888888888888088888888808008t;iiiiii111111i11111ii111iiiii;;;ii;;\n");
		sb1.append(";;;;;;;;iiiiiiiii;;;;;;iiii;;iiiG88888888888888888888888888000000G;;iiiiii1ii;;;;;ii;;;;;i;;ii;;;;;;;;\n");
		sb1.append(";;;;:::;;;;;ii;;;;ii;;;;iiiiiiit0088888888888888888888888800800000t;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;\n");
		sb1.append(";;;;;:;;;;;;;;;;;;;;;;;;;;;;i;;000888888888888888888888888808800000;;;;;;;;;;;;;;;;;;:::;;::;;;::;::;;\n");
		sb1.append(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;L800888888888888888888888888800000000f;;;;;;;;;;;::::::::::::;;;;;;;;;;;\n");
		sb1.append(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;08888888888888888888888888888008888888;;;;;;;;;;::::::::::::;;;;;;;;;;;;\n");
		sb1.append(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;t88888888888888888888888888888888888888f:;;;;;;;;;::::::::::;;;;;;;;;;;;;\n");
		sb1.append("iiiiiii;i;;;;;;;;;;;;;;;;;;;10888888888888888888888888888888088888888t:;;;;;;;;;:::::::;;;;;;;;;;;;;;i\n");
		sb1.append("iiiiiiiii;;;;;;;;;;;;;;;;;i;C88888888888888888888888888888880888888888;;;;;;;;;;::::::;;;;:::::;;;;;;i\n");
		sb1.append("iiiiiiiiiii;;;;;;;;;;;;;;;;L8888888888888888888888888888888888888888880;;;;;;;;;;;;;;;;;:;;::;::;;;;;i\n");
		sb1.append("iiiiiiiiiii;;;;;;;;i;;;;;;;;f088888888888888888880888888888880888888888G;;;;;;;;;;;;;;;;;;;;;;;;;;;;;i\n");
		sb1.append("iiiiiiiiiiiiiiiiiiii;;;;;;;fG080888888888888888888888888888888888888880fiiiiiiiii;;;;;;;;;;;;;;;;;;;;i\n");
		sb1.append("iiiiiiiiiiiiiiiiiiiifCi1G8008888808888888888888888880888888888888888800Ciiiiiiiiiiiiiiiii;;;;;;;;;;;;i\n");
		sb1.append("iiiiiiiiiiiiiiiiiL000000888888888C0888888888888888808888888888888888888880Littiiiiiiiiiiiii;;iiiiiii;i\n");
		sb1.append("iiiiiiiiiiiiiiiL000000000GCL1G8888888888888888888888888888888888888000080080000Ciiiiiiiiiii;;;;;iiiiii\n");
		sb1.append("ii;;;;;;;;;ii100000L1iiii;f88888888888888888888888880888888888888888000LCG08000001iiiiii;;;;;;;;;;;iii\n");
		sb1.append(";;;;;;;;;;;;;10000fi;fGiL888888888888888888888888888088888888888880888800t;i1fG00G;;;;;;;;;;;;;;;;;;;i\n");
		sb1.append(";;;;;;::::;:1G0Gt;;t008888888008888888888888888888888888888888888808888888L1GG800G;;;;;;;;;::::::;;;;i\n");
		sb1.append(";::::::,:::;;:::;;tG0080880Cif888888888888888888888888888888888888888008888800GG0t;;;::::::::::::::;;;\n");
		sb1.append(":::::,,,,::::::::;G000C1;;CGC0880088888888888888888888888888888888888GGLC8800001:1i:::::::::::,::::::;\n");
		sb1.append(",,,,,,,,,,,,,,,,,;000CL::G0888888008C:G888888888888888888888888888880GG08:,L80Gf,:,,,,,,,,,,,,,,,:::::\n");
		sb1.append(",,,,,,,,,,,,,,,,,,1CG0:,LG008800GCC:,,:G888888888888888888888888880000088tf0000;,,,,,,,,,,,,,,,,,,,,,:\n");
		sb1.append(",,,,,,,,,,,,,,,,,,:01,,,f00000L::,,,,,,i@88888888888888888888888:;t0888081.C0C:,,,,,,,,,,,,,,,,,,,,,,:\n");
		sb1.append(",,,,,,,,,,,,,,,,,,,,,,,,;000:,,,,,,:;f88888888888888088888888880t,,,;G800;,,1t,,,,,,,,,,,,,,,,,,,,,,,:\n");
		sb1.append(",,,,,,,,,,,,,,,,,,,,,,,,,C000f;,,,L88888888888888888C888888888888t,,,G8G:,,,,,,,,,,,,,,,,,,,,,,,,,,,,,\n");
		sb1.append(",,,,,,,,,,,,,,,,,,,,,,,,,,;00t:,,f88888888888888888808888888888888888Gf;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:\n");
		
		StringBuilder sb2 = new StringBuilder();
		sb2.append(",,,,,,,,,,,,,,,,,,,,,,,,,,,i0;,,:888888888888888888808888888888888880:,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:\n");
		sb2.append(",,,,,,:,,::,::,,,,,::::::::::,,:G08888888888888888880888888888888880;::::::::::,,::::::::::::,,,,,,:,:\n");
		sb2.append(",:::,:::::::::::::::::::::::::,L00888888888888888888808888888888888C::::::::::::::::::::::::::::::::::\n");
		sb2.append("::::::::::::::::::::::::::::::,LGG888888888888888888C88888888880000Gi::::::::::::::::::::::::::::::::;\n");
		sb2.append("::::::::::::::::::::::::::::::iG0G880888888888888888888888888888080Gt:::::::::::::::::::::::::::::::::\n");
		sb2.append(":::::::::::::::::::::::::::::,LGG0000008888888888888888888888880000Ct;::::::::::::::::::::::::::::::::\n");
		sb2.append(":::::::::::::::::::::::::::::;GG008000888888888888888888888888880000f;::::::::::::::::::::::::::::::::\n");
		sb2.append("::::::::::::::::::::::::::::,1G000G00888888888888888888888888888000GGi,:::::::::::::::::::::::::::::::\n");
		sb2.append(":::::::::::::::::::::::::::,:LG000000088888888888888888880G08888800001,,:::::::::::::::::::,::::::::,:\n");
		sb2.append("::::::::::::::::::::,:::,,:::G000000808888C08888888888888888888888800f,,,,,,,,,:,,,,,::,,,,,,,,,,,,,,:\n");
		sb2.append("::::::::::::::,:::::::::::::;G000000888880CG08888888888888CGC888888888i,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:\n");
		sb2.append(":::::::::,,,,,,,,,,::::::,,;G000000000000f,,G0888888888888Ci1888888800G,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,:\n");
		sb2.append(",,,:::,,,,,,,,,,,,,,,,,,,,,1G000000000000C,,G0080888888880G1G88888880000G,,,,,,,,,,,,,,,,,,,,,,,,,,,,:\n");
		sb2.append(",,,,,,,,,,,,,,,,,,,,,,,,,,,LG00000000000f,,,L00088888888008:,;8888880000Gi,,,,,,,,,,,,,,,,,,,,,,,,,,,:\n");
		sb2.append("::::,,,,,,,,,,,,,,,,:::::,;0G00000000000f,::,C000008888000t,,,C000000000G1,:,::::::::,,,,,::::::::::::\n");
		sb2.append(";;;;;::::,,,,,,,,,,,,,,,:,tCG000000000f1,::::iG00008888000i,,,,:00000000Gf,::::::::::::::::::;;;;;;;;;\n");
		sb2.append(";;;;;:::::::,,,,,,,,,,,,,,1G000000000C;1,::::;0G0000888000t::::t10000000GL:::::;;;;;;;::::;;;;;;;;;;;;\n");
		sb2.append(";;;;;;::::::::,,,,::::::::tCG0000000G:L,::::::LG0000000800;::::f1C000000GC:::;;;;;;;;;;;;;;;;;;;;;;;;;\n");
		sb2.append(";;;;;;;;;;;::::::::::::;;:tCG0000000t;i::::;;;:GG00000000f:;;;;:ff000000GL:;;;;;;;;;;;;;;;;;;;;;;;;;;;\n");
		sb2.append(";;;;;;;;;;;;;::::::::::;;;1CG0000000t1::::::;:::CG000000G;;;;;;;1tGG00000f;;;;;;;;;;;;;;;;;;;;;iiiii;;\n");
		sb2.append(";;;;;;;;;;;;;;;;;;:;::;;;;iCG0000000;:::::::::::CG000000G::::;::::CG000001;;;;;;;;;;;;;;;;;;;;;iiiii;i\n");
		sb2.append("iiiii;;;;;;;;;;;;;;;;;;;;;;CGG00000Cf:::;:::::::LG000000L::::::::iCG000GC;;;;;;;;;;;;;;;;;;;;;;iiiiiii\n");
		sb2.append("11111iiii;;;;;;;;;;;;;;;;;;tCG0000GL:,,:::::::::;GG0000Ci::,:::::t1G000Gt;;;;;;;;;iiiiii;;;iiiiiiiiii1\n");
		sb2.append("111111111i;;;;i;;;;;;;;;;;;iLGG000011,:::::,,:::,LG000GL,:,,,::;;t1G00GC;;;;;;;;iiiiiiiiiiiii111i111i1\n");
		sb2.append("1111111111iiiiiii;;;;;;;;;;;;LGGGGGG:::;;::::::::,tGGGG;:::,,,:;;tC000L;;;;;;;;ii11iii1iii11111111111t\n");
		sb2.append("111111111111111111ii;iiii;;;;iCGGGGG;:::::::::::,11000t:1::::;;;;tGGGGGiiiiiiii111111111111111111111fL\n");
		sb2.append("t111111111111111111111iiiii;;ifGGGGGft:::::::::::11000ft;;;;;;;;1CG0GGCt11111111111111111111111ttt1tLC\n");
		sb2.append("Lftt1111111111111111111iiiiiiii1CGCLGL;i:::::;::;;GG00C1;;;;ii;;LG0GGfi111111111111111111111111tLCfCCG\n");
		sb2.append("CLCft1111111111111111111111111111tCGLf1i;::;;;;;;fGGGGGi:;;ii111GGGf111111111111111111111tttfttfGGG0GG\n");
		sb2.append("GG0GGLLLfLLfttt11111111111111111111fCCti;;;;iiiiitGGGGC:;;i111LGCt1111111111111tttt111ttfLCCG088000000\n");
		sb2.append("000000GGGGGGCCCft111111111111111111111CLtiiiii1i11GGGG1ii111fCf1111111111111ttfLCCLLCCGG00000000000000\n");
		sb2.append("000000000000000GCftt111t11t1111111111111Gft1111iiiCGGf1111LCt111111tttffffLCLCGG00G0000000000800000000\n");
		sb2.append("00000000000000000GCLfLCLCCCLLfttftttt11111t1111111tGL1111111ttttttfLCGCG000000000000000080800000000000\n");
		sb2.append("00000000000000000000000000GGGCCGGGGCLLft1111111ttfLCftttttffLLffCCG00000000000888080000000000000000000\n");
		sb2.append("0000000000000000000000000000000000000GGCLfftfCLLLCCGG0GGCCGGG0G000000000000008000000000000000000000000\n");
		sb2.append("000000000000000000800000000000000000000000000000000000000000000000000000000000000000008000000000000000\n\n");
		sb2.append("\n");	
		return sb1.toString() + sb2.toString();
	}
	
	/**
	 * Displays a reaper in the logcat
     * @param lvl - The level of display, related to the color use in the console
	 */
	public static void displayReaper( LogLevel lvl ){
		log(lvl, "Augure", createReaper());
	}
	
	/**
	 * Creates the Joly Roger
	 * @return String - The ASCII-art Joly Roger
	 */
	private static String createJolyRoger(){
		StringBuilder sb = new StringBuilder();
		sb.append("  _                   _ \n");
		sb.append(" _( )                 ( )_ \n");
		sb.append("(_, |      __ __      | ,_) \n");
		sb.append("   \\'\\    /  ^  \\    /'/ \n");
		sb.append("    '\\'\\,/\\      \\,/'/' \n");
		sb.append("      '\\| []   [] |/' \n");
		sb.append("        (_  /^\\  _) \n");
		sb.append("          \\  ~  / \n");
		sb.append("          /HHHHH\\' \n");
		sb.append("        /'/{^^^}\\'\\' \n");
		sb.append("    _,/'/'  ^^^  '\\'\\,_ \n");
		sb.append("   (_, |           | ,_) \n");
		sb.append("     (_)           (_) \n");
		return sb.toString();
	}

	/**
	 * Displays in the logcat the Joly Roger
     * @param lvl - The level of display, related to the color use in the console
	 */
	public static void displayJolyRoger( LogLevel lvl ){
		log(lvl, "JollyRogers", createJolyRoger());
	}


    /* *********** *
     * INNER ENUMS *
     * *********** */

	/**
	 * Les différents niveaux de log
	 */
	public enum LogLevel {
		/**
		 * Black
		 */
		VERBOSE,
		/**
		 * Green
		 */
		INFORMATION,
		/**
		 * Bleu
		 */
		DEBUG,
		/**
		 * Orange
		 */
		WARNING,
		/**
		 * Red
		 */
		ERROR,
		/**
		 * Dalek
		 */
		FATAL_ERROR,
		/**
		 * Reaper
		 */
		WTF_ERROR
	}
	
}

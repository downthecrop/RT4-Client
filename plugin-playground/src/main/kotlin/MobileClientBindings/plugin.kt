package MobileClientBindings

import plugin.Plugin
import plugin.annotations.PluginMeta
import plugin.api.*
import rt4.Keyboard
import rt4.Mouse
import java.awt.event.*
import javax.swing.SwingUtilities


@PluginMeta(
    author = "downthecrop",
    description = "Ported modifications from legacy mobile miniclient",
    version = 1.0
)
class plugin : Plugin() {

    companion object {
        private var cameraToggle = false;
        private var rightClickToggle = false;
        private var lastMouseWheelX = 0
        private var lastMouseWheelY = 0
        private val defaultCameraPYZ = Triple(128.0, 0.0, 600)
    }

    override fun Init() {
        // Unregister the default keyboard listener
        Keyboard.stop(rt4.GameShell.canvas)
        API.AddKeyboardListener(KeyboardCallback)
        API.AddMouseListener(MouseCallbacks)
    }

    object MouseCallbacks : MouseAdapter() {
        override fun mouseMoved(e: MouseEvent?) {
            e ?: return
            if (cameraToggle) {
                val x = e.x
                val y = e.y
                val accelX = lastMouseWheelX - x
                val accelY = lastMouseWheelY - y
                lastMouseWheelX = x
                lastMouseWheelY = y
                API.UpdateCameraYaw(accelX * 2.0)
                API.UpdateCameraPitch(-accelY * 2.0)
            }
        }
        override fun mouseDragged(e: MouseEvent?) {
            e ?: return
            if (cameraToggle) {
                val x = e.x
                val y = e.y
                val accelX = lastMouseWheelX - x
                val accelY = lastMouseWheelY - y
                lastMouseWheelX = x
                lastMouseWheelY = y
                API.UpdateCameraYaw(accelX * 2.0)
                API.UpdateCameraPitch(-accelY * 2.0)
            }
        }

        override fun mouseClicked(e: MouseEvent?) {
            e ?: return

            val width = API.GetWindowDimensions().width;
            val compassBordersX = intArrayOf(width - 165, width - 125)
            val compassBordersY = intArrayOf(0, 45)

            if (
                e.x in compassBordersX[0]..compassBordersX[1]
                && e.y in compassBordersY[0]..compassBordersY[1]
            )
            {
                API.SetCameraPitch(defaultCameraPYZ.first)
                API.SetCameraYaw(defaultCameraPYZ.second)
            }
        }

        override fun mousePressed(e: MouseEvent?) {
            e ?: return
            if (cameraToggle) {
                lastMouseWheelX = e.x
                lastMouseWheelY = e.y
            }
            if (SwingUtilities.isMiddleMouseButton(e)) {
                return
            }

            if (Mouse.instance != null) {
                Mouse.anInt2467 = 0
                Mouse.anInt1034 = e.x
                Mouse.anInt4973 = e.y
                Mouse.aLong161 = MonotonicClock.currentTimeMillis()
                if (e.modifiersEx and MouseEvent.BUTTON3_DOWN_MASK == 0 && !rightClickToggle) {
                    Mouse.anInt1313 = 1
                    Mouse.anInt1759 = 1
                } else {
                    rightClickToggle = false;
                    Mouse.anInt1313 = 2
                    Mouse.anInt1759 = 2
                }
            }
            if (e.isPopupTrigger) {
                e.consume()
            }
        }
    }

    object KeyboardCallback : KeyListener {

        private var capitalize = false

        override fun keyTyped(e: KeyEvent?) {
            // The default RT4 keyboard logic.
            // for printable characters.
            if(e == null) return

            if(capitalize){
                capitalize = false;
                println("Replaced char: "+ e.keyChar);
                if(isSpecial(e.keyChar)){
                    e.keyChar = getSpecial(e.keyChar);
                } else {
                    e.keyChar = Character.toUpperCase(e.keyChar);
                }
                println("With char: "+ e.keyChar);
            }

            val c = Keyboard.getKeyChar(e)
            println("WRITING: "+e.keyChar);
            if (c >= 0) {
                val index = Keyboard.typedQueueWriterIndex + 1 and 0x7F
                if (Keyboard.typedQueueReaderIndex != index) {
                    Keyboard.typedCodeQueue[Keyboard.typedQueueWriterIndex] = -1
                    Keyboard.typedCharQueue[Keyboard.typedQueueWriterIndex] = c
                    Keyboard.typedQueueWriterIndex = index
                }
            }
            e.consume()
        }

        override fun keyPressed(e: KeyEvent?) {
            // The default RT4 keyboard logic.
            // for delete key ect.
            if(e == null) return

            if (e.keyCode == KeyEvent.VK_F12) {
                capitalize = true;
                return;
            }

            if(e.keyCode == KeyEvent.VK_F9) {
                cameraToggle = true;
                return;
            }
            if(e.keyCode == KeyEvent.VK_F8) {
                cameraToggle = false;
                return;
            }
            if(e.keyCode == KeyEvent.VK_F10) {
                rightClickToggle = false;
                return;
            }
            if(e.keyCode == KeyEvent.VK_F11) {
                rightClickToggle = true;
                return;
            }

            Keyboard.idleLoops = 0
            var code: Int = e.keyCode
            if (code >= 0 && Keyboard.CODE_MAP.size > code) {
                code = Keyboard.CODE_MAP[code]
                if (code and 0x80 != 0) {
                    code = -1
                }
            } else {
                code = -1
            }

            if (Keyboard.eventQueueWriterIndex >= 0 && code >= 0) {
                Keyboard.eventQueue[Keyboard.eventQueueWriterIndex] = code
                Keyboard.eventQueueWriterIndex = Keyboard.eventQueueWriterIndex + 1 and 0x7F
                if (Keyboard.eventQueueWriterIndex == Keyboard.eventQueueReaderIndex) {
                    Keyboard.eventQueueWriterIndex = -1
                }
            }

            if (code >= 0) {

                val index = Keyboard.typedQueueWriterIndex + 1 and 0x7F
                if (index != Keyboard.typedQueueReaderIndex) {
                    Keyboard.typedCodeQueue[Keyboard.typedQueueWriterIndex] = code
                    Keyboard.typedCharQueue[Keyboard.typedQueueWriterIndex] = -1
                    Keyboard.typedQueueWriterIndex = index
                }
            }

            val modifiers: Int = e.modifiersEx
            if (modifiers and 0xA != 0 || code == Keyboard.KEY_BACK_SPACE || code == Keyboard.KEY_ENTER) {
                e.consume()
            }
        }

        override fun keyReleased(e: KeyEvent?) {

        }

        fun getSpecial(c: Char): Char {
            when (c) {
                '1' -> return '!'
                '2' -> return '@'
                '3' -> return '#'
                '4' -> return '$'
                '5' -> return '%'
                '6' -> return '^'
                '7' -> return '&'
                '8' -> return '*'
                '9' -> return '('
                '0' -> return ')'
                '-' -> return '_'
                '=' -> return '+'
                '[' -> return '{'
                ']' -> return '}'
                ';' -> return ':'
                '\'' -> return '"'
                ',' -> return '<'
                '.' -> return '>'
                '/' -> return '?'
                '\\' -> return '|'
            }
            return '~'
        }

        fun isSpecial(c: Char): Boolean {
            val specialChars = "/*!@#$%^&*:();\"{}_[+=-_]\'|\\?/<>,."
            return Character.isDigit(c) || specialChars.contains("" + c)
        }
    }

    object MonotonicClock {
        private var leapMillis: Long = 0

        private var previous: Long = 0
        @Synchronized
        fun currentTimeMillis(): Long {
            val now = System.currentTimeMillis()
            if (previous > now) {
                leapMillis += previous - now
            }
            previous = now
            return leapMillis + now
        }
    }

    /*
    key listeners
       public final synchronized void keyPressed(KeyEvent var1) {
      try {
         System.out.println("Key code: " + var1.getKeyCode());
         switch (var1.getKeyCode())
         {
            case 16:
               MouseWheel.shiftDown = true;
               break;
            case 17:
               MouseWheel.ctrlDown = true;
               break;
            case 116:
               Client.ZOOM -= 20;
               return;
            case 115:
               Client.ZOOM += 20;
               return;
            case 120:
               //Cam On
               new isMiddleMouse(true);
               return;
            case 119:
               //Cam Off
               new isMiddleMouse(false);
               return;
            case 121:
               if(isRightClick.getRC()){
                  new isRightClick(false);
               }
               return;
            case 122:
               if(!isRightClick.getRC()){
                  new isRightClick(true);
               }
               return;
            case 123:
               capitalize = true;
               return;


     */



    /*
    fun soemthing() {
        System.out.println("Key code: " + var1.getKeyCode())
        when (var1.getKeyCode()) {
            16 -> MouseWheel.shiftDown = true
            17 -> MouseWheel.ctrlDown = true
            116 -> {
                Client.ZOOM -= 20
                return
            }
            115 -> {
                Client.ZOOM += 20
                return
            }
            120 -> {
                //Cam On
                isMiddleMouse(true)
                return
            }
            119 -> {
                //Cam Off
                isMiddleMouse(false)
                return
            }
            121 -> {
                if (isRightClick.getRC()) {
                    isRightClick(false)
                }
                return
            }
            122 -> {
                if (!isRightClick.getRC()) {
                    isRightClick(true)
                }
                return
            }
            123 -> {
                capitalize = true
                return
            }
        }

        if(capitalize){
            capitalize = false;
            System.out.println("Replacing"+var1.getKeyCode());
            System.out.println("Replacing"+var1.getKeyChar());
            if(isSpecial(var1.getKeyChar())){
                var1.setKeyChar(getSpecial(var1.getKeyChar()));
            } else {
                var1.setKeyChar(Character.toUpperCase(var1.getKeyChar()));
            }
            System.out.println("with"+var1.getKeyChar());
        }
    }

    public class isMiddleMouse {
        private static boolean rcState = false;
        public static synchronized boolean getRC(){
            return rcState;
        }
        public isMiddleMouse(boolean state){
            rcState = state;
        }
    }
}
*/


}
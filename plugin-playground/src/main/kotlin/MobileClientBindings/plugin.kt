package MobileClientBindings

import plugin.Plugin
import plugin.annotations.PluginMeta
import plugin.api.*
import rt4.Keyboard
import java.awt.event.KeyEvent
import java.awt.event.KeyListener


@PluginMeta(
    author = "downthecrop",
    description = "Ported modifications from legacy mobile miniclient",
    version = 1.0
)
class plugin : Plugin() {

    override fun Init() {
        // Unregister the default keyboard listener
        Keyboard.stop(rt4.GameShell.canvas)
        API.AddKeyboardListener(KeyboardCallback)
    }

    object KeyboardCallback : KeyListener {

        private var capitalize = false

        override fun keyTyped(e: KeyEvent?) {
            // The default RT4 keyboard logic.
            // for printable characters.
            if(e == null) return

            if(capitalize){
                capitalize = false;
                println("Replacing"+e.keyCode);
                println("Replacing"+e.keyChar);
                if(isSpecial(e.keyChar)){
                    e.keyChar = getSpecial(e.keyChar);
                } else {
                    e.keyChar = Character.toUpperCase(e.keyChar);
                }
                println("with"+e.keyChar);
            }


            val c = Keyboard.getKeyChar(e)
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
                capitalize = !capitalize
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
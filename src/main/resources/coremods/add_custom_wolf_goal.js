function initializeCoreMod() {
    return {
        "add_custom_wolf_goal": {
            "target": {
                "type": "METHOD",
                "class": "net.minecraft.world.entity.animal.Wolf",
                "methodName": "m_8099_",
                "methodDesc": "()V"
            },
            "transformer": function (method) {
                var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');

                var Opcodes = Java.type('jdk.internal.org.objectweb.asm.Opcodes')
                var beginning = ASMAPI.findFirstInstruction(method, Opcodes.ALOAD, 0)

                method.insert()

                ASMAPI.log('INFO', 'Adding \'add_custom_wolf_goal\' ASM patch...');

                return method;
            }
        }
    }
}
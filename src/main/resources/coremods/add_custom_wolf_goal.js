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

                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
                var beginning = ASMAPI.findFirstInstruction(method, Opcodes.RETURN)

                if (beginning != null) {
                    method.instructions.insert(beginning.getPrevious(), new VarInsnNode(Opcodes.ALOAD, 0))

                    method.instructions.insert(beginning.getPrevious(), ASMAPI.buildMethodCall(
                        'io/github/communitymod/common/entities/ThrownStickEntity',
                        'registerGoal',
                        '(Lnet/minecraft/world/entity/animal/Wolf;)V',
                        ASMAPI.MethodType.STATIC))
                }

                ASMAPI.log('INFO', 'Adding \'add_custom_wolf_goal\' ASM patch...');

                return method;
            }
        }
    }
}
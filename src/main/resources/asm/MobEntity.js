var asmapi = Java.type('net.minecraftforge.coremod.api.ASMAPI')
var opc = Java.type('org.objectweb.asm.Opcodes')
var Label = Java.type('org.objectweb.asm.tree.LabelNode')
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode')
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode')
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode')
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode')

function initializeCoreMod() {
    return {
    	'MobEntity': {
    		'target': {
    			'type': 'CLASS',
    			'name': 'net.minecraft.entity.MobEntity'
    		},
    		'transformer': function(classNode) {
    			var count = 0
    			var fn = asmapi.mapMethod('func_184230_a') // processInitialInteract
    			for (var i = 0; i < classNode.methods.size(); ++i) {
    				var obj = classNode.methods.get(i)
    				if (obj.name == fn) {
    					fix_PII(obj)
    					count++
    				}
    			}
    			if (count < 1)
    				asmapi.log("ERROR", "Failed to modify MobEntity: Method not found")
    			return classNode
    		}
    	}
    }
}

function fix_PII(obj) {
	var fn = asmapi.mapMethod('func_70089_S') // isAlive
	node = asmapi.findFirstMethodCall(obj, asmapi.MethodType.VIRTUAL, "net/minecraft/entity/MobEntity", fn, "()Z")
	if (node) {
		var node2 = node.getNext()
		node2 = node2.getNext()
		while (node2.getOpcode() == -1)
    		node2 = node2.getNext()
		var op9 = new Label()
		var op1 = new VarInsnNode(opc.ALOAD, 0)
		var op2 = new TypeInsnNode(opc.INSTANCEOF, "com/lupicus/syp/entity/IDying")
		var op3 = new JumpInsnNode(opc.IFEQ, op9)
		var op4 = new VarInsnNode(opc.ALOAD, 0)
		var op5 = new VarInsnNode(opc.ALOAD, 1)
		var op6 = new VarInsnNode(opc.ALOAD, 2)
		var op7 = asmapi.buildMethodCall("com/lupicus/syp/entity/IDying", "dyingInteract", "(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResultType;", asmapi.MethodType.INTERFACE)
		var op8 = new InsnNode(opc.ARETURN)
		var list = asmapi.listOf(op1, op2, op3, op4, op5, op6, op7, op8, op9)
		obj.instructions.insertBefore(node2, list)
	}
	else
		asmapi.log("ERROR", "Unable to find call")
}

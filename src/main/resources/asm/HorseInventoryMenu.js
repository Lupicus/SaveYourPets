var asmapi = Java.type('net.minecraftforge.coremod.api.ASMAPI')
var opc = Java.type('org.objectweb.asm.Opcodes')
var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode')
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode')
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode')
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode')
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode')
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode')

function initializeCoreMod() {
    return {
    	'HorseInventoryMenu': {
    		'target': {
    			'type': 'CLASS',
    			'name': 'net.minecraft.world.inventory.HorseInventoryMenu'
    		},
    		'transformer': function(classNode) {
    			var count = 0
    			var fn = "stillValid"
    			for (var i = 0; i < classNode.methods.size(); ++i) {
    				var obj = classNode.methods.get(i)
    				if (obj.name == fn) {
    					fix_CIW(obj)
    					count++
    				}
    			}
    			if (count < 1)
    				asmapi.log("ERROR", "Failed to modify HorseInventoryMenu: Method not found")
    			return classNode
    		}
    	}
    }
}

function fix_CIW(obj) {
	var fn = "isAlive"
	var node = asmapi.findFirstMethodCall(obj, asmapi.MethodType.VIRTUAL, "net/minecraft/world/entity/animal/horse/AbstractHorse", fn, "()Z")
	if (node) {
		var fld = "horse"
		var node2 = node.getNext()
		var label2 = node2.label
		var op10 = new LabelNode()
		var op1 = new JumpInsnNode(opc.IFNE, op10)
		var op2 = new VarInsnNode(opc.ALOAD, 0)
		var op3 = new FieldInsnNode(opc.GETFIELD, "net/minecraft/world/inventory/HorseInventoryMenu", fld, "Lnet/minecraft/world/entity/animal/horse/AbstractHorse;")
		var op4 = new TypeInsnNode(opc.INSTANCEOF, "com/lupicus/syp/entity/IDying")
		var op5 = new JumpInsnNode(opc.IFEQ, label2)
		var op6 = new VarInsnNode(opc.ALOAD, 0)
		var op7 = new FieldInsnNode(opc.GETFIELD, "net/minecraft/world/inventory/HorseInventoryMenu", fld, "Lnet/minecraft/world/entity/animal/horse/AbstractHorse;")
		var op8 = asmapi.buildMethodCall("com/lupicus/syp/entity/IDying", "isDying", "()Z", asmapi.MethodType.INTERFACE)
		var op9 = new JumpInsnNode(opc.IFEQ, label2)
		var list = asmapi.listOf(op1, op2, op3, op4, op5, op6, op7, op8, op9, op10)
		obj.instructions.insertBefore(node2, list)
		obj.instructions.remove(node2)
	}
	else
		asmapi.log("ERROR", "Unable to find call")
}

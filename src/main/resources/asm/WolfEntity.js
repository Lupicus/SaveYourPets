var asmapi = Java.type('net.minecraftforge.coremod.api.ASMAPI')

function initializeCoreMod() {
    return {
    	'WolfEntity': {
    		'target': {
    			'type': 'CLASS',
    			'name': 'net.minecraft.entity.passive.WolfEntity'
    		},
    		'transformer': function(classNode) {
    			if (classNode.superName == "net/minecraft/entity/passive/TameableEntity") {
    				classNode.superName = "com/lupicus/syp/entity/DyingEntity"
    				for (var i = 0; i < classNode.methods.size(); ++i) {
    					var obj = classNode.methods.get(i)
    					if (obj.name == "<init>") {
    						fixSuperCall(obj)
    					}
    				}
    			}
    			else
    				asmapi.log("WARN", "WolfEntity might die (" + classNode.superName + ")")
    			return classNode
    		}
    	}
    }
}

function fixSuperCall(obj) {
	var call = asmapi.findFirstMethodCall(obj, asmapi.MethodType.SPECIAL, "net/minecraft/entity/passive/TameableEntity", "<init>", "(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V")
	if (call != null)
		call.owner = "com/lupicus/syp/entity/DyingEntity"
}

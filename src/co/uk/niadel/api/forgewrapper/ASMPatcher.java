//Commented out until I know what to do with this.

/*
package co.uk.niadel.api.forgewrapper;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public class ASMPatcher implements IClassTransformer
{	
	public byte[] transform(String currClassName, String newClassName, byte[] bytes)
	{
		switch (currClassName)
		{
			case "abn":
				patchClassObfed(currClassName, newClassName, bytes);
		}
	}
	
	public byte[] patchClassObfed(String currClassName, String newClassName, byte[] bytes)
	{
		ClassNode cn = new ClassNode();
		ClassReader cr = new ClassReader(bytes);
		ClassWriter cw = new ClassWriter(cr, Opcodes.ASM4);
		cr.accept(cn, 0);
		
		switch (currClassName)
		{
			case "abn":
				cw.newField("abn", "instance", "public Labn;");
				FieldVisitor fv = cw.visitField(Opcodes.ACC_PUBLIC, "instance", "public Labn", null, null);
				
		}
	}
}
*/
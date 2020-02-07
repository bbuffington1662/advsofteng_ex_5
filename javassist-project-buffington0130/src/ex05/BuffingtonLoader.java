package ex05;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;

public class BuffingtonLoader extends ClassLoader {
   static String WORK_DIR = System.getProperty("user.dir");
   static String INPUT_DIR = WORK_DIR + File.separator + "classfiles";
   public static String TARGET_APP = null;
   public static String hiddenValue = null;
   private ClassPool pool;

   public static void main(String[] args) throws Throwable {
	   Scanner input = new Scanner(System.in);
		String[] values = null;
		
		System.out.print("Please enter the name of the class and member field name to manipulate separtated by commas: ");
		values = input.nextLine().split(",");
		input.close();
		TARGET_APP = values[0];
	    hiddenValue = values[1];
		BuffingtonLoader loader = new BuffingtonLoader();
		TARGET_APP = values[0];
	    hiddenValue = values[1];
	    args = values;
		Class<?> c = loader.loadClass(TARGET_APP);
        c.getDeclaredMethod("main", new Class[] { String[].class }). //
            invoke(null, new Object[] { args });
   }

   public BuffingtonLoader() throws NotFoundException {
      pool = new ClassPool();
      pool.insertClassPath(INPUT_DIR); // Search MyApp.class in this path.
   }

   /* 
    * Find a specified class, and modify the bytecode.
    */
   protected Class<?> findClass(String name) throws ClassNotFoundException {
      try {
         CtClass cc = pool.get(name);
         if (name.equals(TARGET_APP)) {
            CtField f = new CtField(CtClass.doubleType, hiddenValue, cc);
            f.setModifiers(Modifier.PUBLIC);
            cc.addField(f, CtField.Initializer.constant(0.0));
         }
         byte[] b = cc.toBytecode();
         return defineClass(name, b, 0, b.length);
      } catch (NotFoundException e) {
         throw new ClassNotFoundException();
      } catch (IOException e) {
         throw new ClassNotFoundException();
      } catch (CannotCompileException e) {
         throw new ClassNotFoundException();
      }
   }
}

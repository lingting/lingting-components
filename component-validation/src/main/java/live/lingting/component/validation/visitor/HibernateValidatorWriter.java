package live.lingting.component.validation.visitor;

import javassist.CtClass;
import javassist.CtMethod;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorManager;

/**
 * @author lingting 2022/11/2 11:23
 */
public class HibernateValidatorWriter extends AbstractClassWriter {

	@Override
	public String getClassName() {
		return "org.hibernate.validator.internal.engine.constraintvalidation.ConstraintTree";
	}

	@Override
	public Class<?> getPackageClass() {
		return ConstraintValidatorManager.class;
	}

	@Override
	public void write(CtClass ctClass) throws Exception {
		CtMethod ctMethod = getMethod(ctClass, "validateSingleConstraint");
		ctMethod.setBody("{        boolean isValid;\n" + "         try {\n"
				+ "             Object validatedValue = (Object) $1.getCurrentValidatedValue();\n"
				+ "             // 支持值上下文传入进行的校验\n"
				+ "             if ($3 instanceof live.lingting.component.validation.EntityConstraintValidator) {\n"
				+ "                 isValid = ((live.lingting.component.validation.EntityConstraintValidator) $3).isValid(validatedValue, $2, $1);\n"
				+ "             } else {\n" + "                 isValid = $3.isValid(validatedValue, $2);\n"
				+ "             }\n" + "         } catch (RuntimeException e) {\n"
				+ "             if (e instanceof javax.validation.ConstraintDeclarationException) {\n"
				+ "                 throw e;\n" + "             }\n"
				+ "             throw LOG.getExceptionDuringIsValidCallException(e);\n" + "         }\n"
				+ "         if (!isValid) {\n"
				+ "             //We do not add these violations yet, since we don't know how they are\n"
				+ "             //going to influence the final boolean evaluation\n"
				+ "             return java.util.Optional.of($2);\n" + "         }\n"
				+ "         return java.util.Optional.empty();}");
	}

}

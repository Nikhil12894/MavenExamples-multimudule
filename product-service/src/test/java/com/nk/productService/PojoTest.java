package com.nk.productService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.openpojo.reflection.filters.FilterPackageInfo;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;

@SpringBootTest
class PojoTest {
  // Configured for expectation, so we know when a class gets added or removed.
//  private static final int EXPECTED_CLASS_COUNT = 1;

  // The package to test
  private static final String POJO_PACKAGE = "com.nk.productService.model";
  private static final String POJO_PACKAGE1 = "com.nk.productService.dto";

//  @Test
//  public void ensureExpectedPojoCount() {
//    List <PojoClass> pojoClasses = PojoClassFactory.getPojoClasses(POJO_PACKAGE,
//                                                                   new FilterPackageInfo());
//    Affirm.affirmEquals("Classes added / removed?", EXPECTED_CLASS_COUNT, pojoClasses.size());
//  }

  @Test
  void testPojoStructureAndBehavior() {
    Validator validator = ValidatorBuilder.create()
                            // Add Rules to validate structure for POJO_PACKAGE
                            // See com.openpojo.validation.rule.impl for more ...
//                            .with(new GetterMustExistRule())
//                            .with(new SetterMustExistRule())
                            // Add Testers to validate behaviour for POJO_PACKAGE
                            // See com.openpojo.validation.test.impl for more ...
                            .with(new SetterTester())
                            .with(new GetterTester())
//                            .with(new ToStringTester())
                            .build();

    validator.validate(POJO_PACKAGE, new FilterPackageInfo());
    validator.validate(POJO_PACKAGE1, new FilterPackageInfo());
  }
}
/*
 * llvm-j  is a library for parsing and modification of LLVM IR in Java.
 * This file is part of llvm-j.
 *
 * Copyright (C) 2017-2018 Marek Chalupa, Dirk Beyer
 * All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.sosy_lab.llvm_j;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ModuleTest {

  private Context context;

  @Before
  public void setUp_library() {
    Path libraryPath = Paths.get("lib", "java", "runtime");
    List<Path> relevantLibDirs = ImmutableList.of(libraryPath);
    Module.addLibraryLookupPaths(relevantLibDirs);
    context = Context.create();
  }

  @After
  public void tearDown_context() {
    context.close();
  }

  @Test
  @SuppressWarnings("deprecation")
  public void test_parseBitcode_noContext_valid() throws LLVMException {
    String llvmFile = "build/test.bc";

    try (Module m = Module.parseIR(llvmFile)) {
      expectComponentsExist(m);
    }
  }

  @Test
  @SuppressWarnings("deprecation")
  public void test_parseLl_noContext_valid() throws LLVMException {
    String llvmFile = "build/test.ll";

    try (Module m = Module.parseIR(llvmFile)) {
      expectComponentsExist(m);
    }
  }

  @Test
  public void test_parseBitcode_withContext_valid() throws LLVMException {
    String llvmFile = "build/test.bc";

    try (Module m = Module.parseIR(llvmFile, context)) {
      expectComponentsExist(m);
    }
  }

  @Test
  public void test_parseLl_withContext_valid() throws LLVMException {
    String llvmFile = "build/test.ll";

    try (Module m = Module.parseIR(llvmFile, context)) {
      expectComponentsExist(m);
    }
  }

  /** Check that basic components of the provided {@link Module} exist. */
  private static void expectComponentsExist(Module pModule) {
    assertThat(pModule).isNotNull();

    Value firstFunction = pModule.getFirstFunction();
    assertThat(firstFunction).isNotNull();

    BasicBlock firstBlock = firstFunction.getFirstBasicBlock();
    assertThat(firstBlock).isNotNull();

    Value firstInstruction = firstBlock.getFirstInstruction();
    assertThat(firstInstruction).isNotNull();
  }
}

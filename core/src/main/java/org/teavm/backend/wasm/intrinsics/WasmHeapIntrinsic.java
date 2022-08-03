/*
 *  Copyright 2016 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.teavm.backend.wasm.intrinsics;

import org.teavm.ast.InvocationExpr;
import org.teavm.backend.wasm.WasmHeap;
import org.teavm.backend.wasm.model.expression.WasmBlock;
import org.teavm.backend.wasm.model.expression.WasmExpression;
import org.teavm.backend.wasm.model.expression.WasmMemoryGrow;
import org.teavm.model.MethodReference;

public class WasmHeapIntrinsic implements WasmIntrinsic {
    private final boolean wasi;

    public WasmHeapIntrinsic(boolean wasi) {
        this.wasi = wasi;
    }

    @Override
    public boolean isApplicable(MethodReference methodReference) {
        if (!methodReference.getClassName().equals(WasmHeap.class.getName())) {
            return false;
        }
        switch (methodReference.getName()) {
            case "initHeapTrace":
                return wasi;
            case "growMemory":
                return true;
            default:
                return false;
        }
    }

    @Override
    public WasmExpression apply(InvocationExpr invocation, WasmIntrinsicManager manager) {
        switch (invocation.getMethod().getName()) {
            case "initHeapTrace":
                return new WasmBlock(false);
            case "growMemory":
                return new WasmMemoryGrow(manager.generate(invocation.getArguments().get(0)));
            default:
                throw new IllegalArgumentException(invocation.getMethod().getName());
        }
    }
}

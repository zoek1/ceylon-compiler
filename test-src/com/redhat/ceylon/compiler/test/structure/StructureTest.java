package com.redhat.ceylon.compiler.test.structure;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import com.redhat.ceylon.compiler.test.CompilerTest;

public class StructureTest extends CompilerTest {
    
    //
    // Packages
    
    @Test
    public void testPkgPackage(){
        compareWithJavaSource("pkg/pkg");
    }

    //
    // Modules
    
    @Test
    public void testMdlModule(){
        compareWithJavaSource("module/single/module");
    }

    @Test
    public void testMdlModuleFromCompiledModule() throws IOException{
        compile("module/single/module.ceylon");
        
        File jarFile = getModuleCar("com.redhat.ceylon.compiler.test.structure.module.single", "6.6.6");
        assertTrue(jarFile.exists());

        JarFile jar = new JarFile(jarFile);
        // just to be sure
        ZipEntry bogusEntry = jar.getEntry("com/redhat/ceylon/compiler/test/structure/module/single/BOGUS");
        assertNull(bogusEntry);

        ZipEntry moduleClass = jar.getEntry("com/redhat/ceylon/compiler/test/structure/module/single/module.class");
        assertNotNull(moduleClass);
        jar.close();

        compile("module/single/subpackage/Subpackage.ceylon");

        // MUST reopen it
        jar = new JarFile(jarFile);

        ZipEntry subpackageClass = jar.getEntry("com/redhat/ceylon/compiler/test/structure/module/single/subpackage/Subpackage.class");
        assertNotNull(subpackageClass);
    }

    private File getModuleCar(String moduleName, String version) {
        return getModuleCar(moduleName, version, destDir);
    }

    private File getModuleCar(String moduleName, String version, String destDir) {
        return getModuleArchiveName(moduleName, version, destDir, "car");
    }

    private File getModuleSourceArchive(String moduleName, String version) {
        return getModuleArchiveName(moduleName, version, destDir, "src");
    }
    
    private File getModuleArchiveName(String moduleName, String version, String destDir, String extension) {
        String modulePath = moduleName.replace('.', File.separatorChar)+File.separatorChar+version+File.separator;
        File jarFile = new File(destDir, modulePath+moduleName+"-"+version+"."+extension);
        return jarFile;
    }

    @Test
    public void testMdlInterdepModule(){
        // first compile it all from source
        compile("module/interdep/a/module.ceylon", "module/interdep/a/b.ceylon", "module/interdep/a/A.ceylon",
                "module/interdep/b/module.ceylon", "module/interdep/b/a.ceylon", "module/interdep/b/B.ceylon");
        
        File jarFile = getModuleCar("com.redhat.ceylon.compiler.test.structure.module.interdep.a", "6.6.6");
        assertTrue(jarFile.exists());

        jarFile = getModuleCar("com.redhat.ceylon.compiler.test.structure.module.interdep.b", "6.6.6");
        assertTrue(jarFile.exists());
        
        // then try to compile only one module (the other being loaded from its car) 
        compile("module/interdep/a/module.ceylon", "module/interdep/a/b.ceylon", "module/interdep/a/A.ceylon");
    }

    @Test
    public void testMdlDependentModule(){
        // Compile only the first module 
        compile("module/depend/a/module.ceylon", "module/depend/a/A.ceylon");
        
        File jarFile = getModuleCar("com.redhat.ceylon.compiler.test.structure.module.depend.a", "6.6.6");
        assertTrue(jarFile.exists());

        // then try to compile only one module (the other being loaded from its car) 
        compile("module/depend/b/module.ceylon", "module/depend/b/a.ceylon");

        jarFile = getModuleCar("com.redhat.ceylon.compiler.test.structure.module.depend.b", "6.6.6");
        assertTrue(jarFile.exists());
    }

    @Test
    public void testMdlMultipleRepos(){
        // Compile the first module in its own repo 
        File repoA = new File("build/ceylon-cars-a");
        repoA.mkdirs();
        Boolean result = getCompilerTask(Arrays.asList("-out", repoA.getPath()),
                "module/depend/a/module.ceylon", "module/depend/a/A.ceylon").call();
        Assert.assertEquals(Boolean.TRUE, result);
        
        File jarFile = getModuleCar("com.redhat.ceylon.compiler.test.structure.module.depend.a", "6.6.6", repoA.getPath());
        assertTrue(jarFile.exists());

        // make another repo for the second module
        File repoB = new File("build/ceylon-cars-b");
        repoB.mkdirs();

        // then try to compile only one module (the other being loaded from its car) 
        result = getCompilerTask(Arrays.asList("-out", repoB.getPath(), "-repo", repoA.getPath()),
                "module/depend/b/module.ceylon", "module/depend/b/a.ceylon", "module/depend/b/B.ceylon").call();
        Assert.assertEquals(Boolean.TRUE, result);

        jarFile = getModuleCar("com.redhat.ceylon.compiler.test.structure.module.depend.b", "6.6.6", repoB.getPath());
        assertTrue(jarFile.exists());

        // make another repo for the third module
        File repoC = new File("build/ceylon-cars-c");
        repoC.mkdirs();

        // then try to compile only one module (the others being loaded from their car) 
        result = getCompilerTask(Arrays.asList("-out", repoC.getPath(), 
                "-repo", repoA.getPath(), "-repo", repoB.getPath()),
                "module/depend/c/module.ceylon", "module/depend/c/a.ceylon", "module/depend/c/b.ceylon").call();
        Assert.assertEquals(Boolean.TRUE, result);

        jarFile = getModuleCar("com.redhat.ceylon.compiler.test.structure.module.depend.c", "6.6.6", repoC.getPath());
        assertTrue(jarFile.exists());
}

    //
    // Classes
    
    @Test
    public void testKlsClass(){
        compareWithJavaSource("klass/Klass");
    }
    @Test
    public void testKlsPublicClass(){
        compareWithJavaSource("klass/PublicKlass");
    }
    @Test
    public void testKlsInterface(){
        compareWithJavaSource("klass/Interface");
    }
    @Test
    public void testKlsInterfaceWithMembers(){
        compareWithJavaSource("klass/InterfaceWithMembers");
    }
    @Test
    public void testKlsInterfaceWithConcreteMembers(){
        compareWithJavaSource("klass/InterfaceWithConcreteMembers");
    }
    @Test
    public void testKlsInitializerParameter(){
        compareWithJavaSource("klass/InitializerParameter");
    }
    @Test
    public void testKlsExtends(){
        compareWithJavaSource("klass/Extends");
    }
    @Test
    public void testKlsSatisfiesErasure(){
        compareWithJavaSource("klass/SatisfiesErasure");
    }
    @Test
    public void testKlsSatisfies(){
        compareWithJavaSource("klass/Satisfies");
    }
    @Test
    public void testKlsSatisfiesWithMembers(){
        compareWithJavaSource("klass/SatisfiesWithMembers");
    }
    @Test
    public void testKlsAbstractFormal(){
        compareWithJavaSource("klass/AbstractFormal");
    }
    @Test
    public void testKlsKlassMethodTypeParams(){
        compareWithJavaSource("klass/KlassMethodTypeParams");
    }
    @Test
    public void testKlsKlassTypeParams(){
        compareWithJavaSource("klass/KlassTypeParams");
    }
    @Test
    public void testKlsKlassTypeParamsSatisfies(){
        compareWithJavaSource("klass/KlassTypeParamsSatisfies");
    }
    @Test
    public void testKlsInnerClass(){
        compareWithJavaSource("klass/InnerClass");
    }
    @Test
    public void testKlsLocalClass(){
        compareWithJavaSource("klass/LocalClass");
    }
    @Test
    public void testKlsInitializerVarargs(){
        compareWithJavaSource("klass/InitializerVarargs");
    }
    @Test
    public void testKlsKlassWithObjectMember(){
        compareWithJavaSource("klass/KlassWithObjectMember");
    }
    @Ignore("M2")
    @Test
    public void testKlsCaseTypes(){
        compareWithJavaSource("klass/CaseTypes");
    }
    
    //
    // Methods
    
    @Test
    public void testMthLocalMethod(){
        compareWithJavaSource("method/LocalMethod");
    }
    @Test
    public void testMthMethod(){
        compareWithJavaSource("method/Method");
    }
    @Test
    public void testMthMethodErasure(){
        compareWithJavaSource("method/MethodErasure");
    }
    @Test
    public void testMthMethodTypeParams(){
        compareWithJavaSource("method/MethodTypeParams");
    }
    @Test
    public void testMthMethodWithLocalObject(){
        compareWithJavaSource("method/MethodWithLocalObject");
    }
    @Test
    public void testMthMethodWithParam(){
        compareWithJavaSource("method/MethodWithParam");
    }
    @Test
    public void testMthMethodWithVarargs(){
        compareWithJavaSource("method/MethodWithVarargs");
    }
    @Test
    public void testMthPublicMethod(){
        compareWithJavaSource("method/PublicMethod");
    }

    //
    // Attributes
    
    @Test
    public void testAtrClassVariable(){
        compareWithJavaSource("attribute/ClassVariable");
    }
    @Test
    public void testAtrClassVariableWithInitializer(){
        compareWithJavaSource("attribute/ClassVariableWithInitializer");
    }
    @Test
    public void testAtrClassAttribute(){
        // FIXME: this one should fail and we should make sure it fails for the right reason
        compareWithJavaSource("attribute/ClassAttribute");
    }
    @Test
    public void testAtrClassAttributeWithInitializer(){
        compareWithJavaSource("attribute/ClassAttributeWithInitializer");
    }
    @Test
    public void testAtrClassAttributeGetter(){
        compareWithJavaSource("attribute/ClassAttributeGetter");
    }
    @Test
    public void testAtrClassAttributeGetterSetter(){
        compareWithJavaSource("attribute/ClassAttributeGetterSetter");
    }
    @Test
    public void testAtrInnerAttributeGetter(){
        compareWithJavaSource("attribute/InnerAttributeGetter");
    }
    @Test
    public void testAtrInnerAttributeGetterSetter(){
        compareWithJavaSource("attribute/InnerAttributeGetterSetter");
    }
    
    //
    // Toplevel
    
    @Test
    public void testTopToplevelAttribute(){
        compareWithJavaSource("toplevel/ToplevelAttribute");
    }
    @Test
    public void testTopToplevelAttributeShared(){
        compareWithJavaSource("toplevel/ToplevelAttributeShared");
    }
    @Test
    public void testTopToplevelVariable(){
        compareWithJavaSource("toplevel/ToplevelVariable");
    }
    @Test
    public void testTopToplevelVariableShared(){
        compareWithJavaSource("toplevel/ToplevelVariableShared");
    }
    @Test
    public void testTopToplevelObject(){
        compareWithJavaSource("toplevel/ToplevelObject");
    }
    @Test
    public void testTopToplevelObjectShared(){
        compareWithJavaSource("toplevel/ToplevelObjectShared");
    }
    @Test
    public void testTopToplevelObjectWithMembers(){
        compareWithJavaSource("toplevel/ToplevelObjectWithMembers");
    }
    @Test
    public void testTopToplevelObjectWithSupertypes(){
        compareWithJavaSource("toplevel/ToplevelObjectWithSupertypes");
    }
    @Test
    public void testTopToplevelMethods(){
        compareWithJavaSource("toplevel/ToplevelMethods");
    }
    
    //
    // Type
    
    @Test
    public void testTypBasicTypes(){
        compareWithJavaSource("type/BasicTypes");
    }
    @Test
    public void testTypConversions(){
        compareWithJavaSource("type/Conversions");
    }
    @Test
    public void testTypOptionalType(){
        compareWithJavaSource("type/OptionalType");
    }
    @Test
    public void testTypSequenceType(){
        compareWithJavaSource("type/SequenceType");
    }
    
    //
    // import
    
    private void compileImportedPackage() {
        compile("import_/pkg/C1.ceylon", "import_/pkg/C2.ceylon");
    }

    @Test
    public void testImpImportAttrSingle(){
        compileImportedPackage();
        compareWithJavaSource("import_/ImportAttrSingle");
    }

    @Test
    public void testImpImportMethodSingle(){
        compileImportedPackage();
        compareWithJavaSource("import_/ImportMethodSingle");
    }
    
    @Test
    public void testImpImportTypeSingle(){
        compileImportedPackage();
        compareWithJavaSource("import_/ImportTypeSingle");
    }
    
    @Test
    public void testImpImportTypeMultiple(){
        compileImportedPackage();
        compareWithJavaSource("import_/ImportTypeMultiple");
    }
    
    @Test
    public void testImpImportTypeAlias(){
        compileImportedPackage();
        compareWithJavaSource("import_/ImportTypeAlias");
    }
    
    @Test
    public void testImpImportWildcard(){
        compileImportedPackage();
        compareWithJavaSource("import_/ImportWildcard");
    }
    
    @Test
    public void testImpImportJavaRuntimeTypeSingle(){
        compileImportedPackage();
        compareWithJavaSource("import_/ImportJavaRuntimeTypeSingle");
    }
}

package com.redhat.ceylon.compiler.java.test.annotations;

@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({})
@interface AnnotationClassQuoting$annotation {
    
    public abstract java.lang.String string();
    
    public abstract java.lang.String $toString();
    
    public abstract int hash();
    
    public abstract long $hashCode();
}
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE})
@interface AnnotationClassQuoting$annotations {
    
    public abstract com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting$annotation[] value();
}
@ceylon.language.Annotation$annotation
class AnnotationClassQuoting implements com.redhat.ceylon.compiler.java.runtime.model.ReifiedType, ceylon.language.metamodel.SequencedAnnotation<com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting, ceylon.language.metamodel.declaration.ClassOrInterfaceDeclaration>, ceylon.language.Cloneable<com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting> {
    
    AnnotationClassQuoting(com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting$annotation anno) {
        this(anno.string(), anno.toString(), anno.hash(), anno.hashCode());
    }
    
    AnnotationClassQuoting(@ceylon.language.Shared$annotation
    @ceylon.language.Actual$annotation
    final java.lang.String string, @ceylon.language.Shared$annotation
    final java.lang.String toString, @ceylon.language.Shared$annotation
    @ceylon.language.Actual$annotation
    final int hash, @ceylon.language.Shared$annotation
    final long hashCode) {
        this.string = string;
        this.toString = toString;
        this.hash = hash;
        this.hashCode = hashCode;
        this.$ceylon$language$metamodel$SequencedAnnotation$this = new ceylon.language.metamodel.SequencedAnnotation$impl<com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting, ceylon.language.metamodel.declaration.ClassOrInterfaceDeclaration>(com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting.$TypeDescriptor, ceylon.language.metamodel.declaration.ClassOrInterfaceDeclaration.$TypeDescriptor, this);
        this.$ceylon$language$metamodel$ConstrainedAnnotation$this = new ceylon.language.metamodel.ConstrainedAnnotation$impl<com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting, ceylon.language.Sequential<? extends com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting>, ceylon.language.metamodel.declaration.ClassOrInterfaceDeclaration>(com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting.$TypeDescriptor, com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor.klass(ceylon.language.Sequential.class, com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting.$TypeDescriptor), ceylon.language.metamodel.declaration.ClassOrInterfaceDeclaration.$TypeDescriptor, this);
        this.$ceylon$language$metamodel$Annotation$this = new ceylon.language.metamodel.Annotation$impl<com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting>(com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting.$TypeDescriptor, this);
        this.$ceylon$language$Cloneable$this = new ceylon.language.Cloneable$impl<com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting>(com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting.$TypeDescriptor, this);
    }
    private final java.lang.String string;
    
    @java.lang.Override
    public final java.lang.String toString() {
        return string;
    }
    private final java.lang.String toString;
    
    public final java.lang.String getToString() {
        return toString;
    }
    private final int hash;
    
    @java.lang.Override
    public final int hashCode() {
        return hash;
    }
    private final long hashCode;
    
    public final long getHashCode() {
        return hashCode;
    }
    protected final ceylon.language.metamodel.SequencedAnnotation$impl<com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting, ceylon.language.metamodel.declaration.ClassOrInterfaceDeclaration> $ceylon$language$metamodel$SequencedAnnotation$this;
    
    @java.lang.Override
    public ceylon.language.metamodel.SequencedAnnotation$impl<com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting, ceylon.language.metamodel.declaration.ClassOrInterfaceDeclaration> $ceylon$language$metamodel$SequencedAnnotation$impl() {
        return $ceylon$language$metamodel$SequencedAnnotation$this;
    }
    protected final ceylon.language.metamodel.ConstrainedAnnotation$impl<com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting, ceylon.language.Sequential<? extends com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting>, ceylon.language.metamodel.declaration.ClassOrInterfaceDeclaration> $ceylon$language$metamodel$ConstrainedAnnotation$this;
    
    @java.lang.Override
    public ceylon.language.metamodel.ConstrainedAnnotation$impl<com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting, ceylon.language.Sequential<? extends com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting>, ceylon.language.metamodel.declaration.ClassOrInterfaceDeclaration> $ceylon$language$metamodel$ConstrainedAnnotation$impl() {
        return $ceylon$language$metamodel$ConstrainedAnnotation$this;
    }
    
    @java.lang.Override
    public final boolean occurs(final ceylon.language.metamodel.Annotated programElement) {
        return $ceylon$language$metamodel$ConstrainedAnnotation$this.occurs(programElement);
    }
    protected final ceylon.language.metamodel.Annotation$impl<com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting> $ceylon$language$metamodel$Annotation$this;
    
    @java.lang.Override
    public ceylon.language.metamodel.Annotation$impl<com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting> $ceylon$language$metamodel$Annotation$impl() {
        return $ceylon$language$metamodel$Annotation$this;
    }
    protected final ceylon.language.Cloneable$impl<com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting> $ceylon$language$Cloneable$this;
    
    @java.lang.Override
    public ceylon.language.Cloneable$impl<com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting> $ceylon$language$Cloneable$impl() {
        return $ceylon$language$Cloneable$this;
    }
    
    @ceylon.language.Shared$annotation
    @ceylon.language.Actual$annotation
    @java.lang.Override
    public final com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting getClone() {
        return (com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting)ceylon.language.nothing_.$get();
    }
    
    @java.lang.Override
    public com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor $getType() {
        return com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting.$TypeDescriptor;
    }
    public static final com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor $TypeDescriptor = com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor.klass(com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting.class);
}
@com.redhat.ceylon.compiler.java.metadata.AnnotationInstantiation(
        arguments = {
                -32768,
                -32768,
                -32768,
                -32768},
        annotationClass = com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting.class)
final class annotationClassQuoting_ {
    
    private annotationClassQuoting_() {
    }
    static final java.lang.String string = "";
    static final java.lang.String toString = "";
    static final int hash = (int)0L;
    static final long hashCode = 0L;
    
    @ceylon.language.Annotation$annotation
    static com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting annotationClassQuoting() {
        return new com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting("", "", (int)0L, 0L);
    }
    
    public static void main(java.lang.String[] args) {
        ceylon.language.process_.$get().setupArguments(args);
        com.redhat.ceylon.compiler.java.test.annotations.annotationClassQuoting_.annotationClassQuoting();
    }
}
@com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting$annotations({@com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting$annotation(
        string = com.redhat.ceylon.compiler.java.test.annotations.annotationClassQuoting_.string,
        $toString = com.redhat.ceylon.compiler.java.test.annotations.annotationClassQuoting_.toString,
        hash = com.redhat.ceylon.compiler.java.test.annotations.annotationClassQuoting_.hash,
        $hashCode = com.redhat.ceylon.compiler.java.test.annotations.annotationClassQuoting_.hashCode)})
class AnnotationClassQuoting_callsite implements com.redhat.ceylon.compiler.java.runtime.model.ReifiedType {
    
    AnnotationClassQuoting_callsite() {
    }
    
    public static void main(java.lang.String[] args) {
        ceylon.language.process_.$get().setupArguments(args);
        new com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting_callsite();
    }
    
    @java.lang.Override
    public com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor $getType() {
        return com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting_callsite.$TypeDescriptor;
    }
    public static final com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor $TypeDescriptor = com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor.klass(com.redhat.ceylon.compiler.java.test.annotations.AnnotationClassQuoting_callsite.class);
}
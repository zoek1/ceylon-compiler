package com.redhat.ceylon.compiler.java.codegen;

import java.util.Map;

import com.redhat.ceylon.model.typechecker.model.Class;
import com.redhat.ceylon.model.typechecker.model.Constructor;
import com.redhat.ceylon.model.typechecker.model.Declaration;

public class CtorDelegation {
    
    private final Constructor ctor;
    private final Declaration extending;
    
    /**
     * 
     * @param ctor The constructor
     * @param extending The declaration of what the constructor extends 
     * (Class, Constructor from superclass, Constructor from this class)
     */
    public CtorDelegation(Constructor ctor, Declaration extending){
        this.ctor = ctor;
        this.extending = extending;
    }

    /**
     * The constructor
     * @return
     */
    public Constructor getConstructor() {
        return ctor;
    }
    
    /**
     * The declaration of what the constructor extends 
     * (Class, Constructor from superclass, Constructor from this class)
     * @return
     */
    public Declaration getExtending() {
        return extending;
    }
    /**
     * The extended Constructor if it is a Constructor. Will be null if extending a Class initializer
     */
    public Constructor getExtendingConstructor() {
        return extending instanceof Constructor ? (Constructor)extending : null;
    }
    /**
     * Is the constructor delegating to another constructor in the same class
     * @return
     */
    public boolean isSelfDelegation() {
        return ctor != null 
                && ((extending instanceof Constructor 
                && ctor.getContainer().equals(extending.getContainer()))
                || (extending instanceof Class
                && ctor.getContainer().equals(extending)));
    }
    /**
     * true if the constructor is extending a non-abstract constructor from the same class
     * @return
     */
    public boolean isConcreteSelfDelegation() {
        return isSelfDelegation() 
                && (extending instanceof Class 
                || !((Constructor)extending).isAbstract());
    }
    /**
     * true if the constructor is extends an abstract constructor from the same class
     * @return
     */
    public boolean isAbstractSelfDelegation() {
        return isSelfDelegation()
                && extending instanceof Constructor
                && ((Constructor)extending).isAbstract();
    }
    /**
     * true if this delegation is delegating to a superclass initializer or constructor,
     * or is delegating to an abstract constructor of the same class
     * @return
     */
    public boolean isAbstractSelfOrSuperDelegation() {
        return !isSelfDelegation() || 
                extending instanceof Constructor && ((Constructor)extending).isAbstract();
    }
    /**
     * Does any other constructor in the given map delegate to the given constructor? 
     */
    public static boolean isDelegatedTo(Map<Constructor, CtorDelegation> allDelegations, Constructor ctor) {
        for (CtorDelegation d : allDelegations.values()) {
            if (ctor.equals(d.getExtending())) {
                return true;
            }
        }
        return false;
    }
}

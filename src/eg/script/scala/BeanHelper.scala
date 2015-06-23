package eg.script.scala

import scala.language.dynamics
import scala.language.experimental.macros

class BeanHelper[T <: AnyRef](prefix: T) extends Dynamic {
  
  def prefix(): T = prefix
  
  def selectDynamic(name: String) = macro BeanHelper.accessorImpl[T]
  def updateDynamic[V](name: String)(value: Any) = macro BeanHelper.mutatorImpl[T]
}

object BeanHelper {
  
  import scala.reflect.macros.whitebox.Context
  
  def accessorImpl[T <: AnyRef: c.WeakTypeTag](c: Context {
      type PrefixType = BeanHelper[T]
    })(name: c.Expr[String]): c.Expr[Any] = {
    
    import c.universe._
    
    val getterNameString = name.tree match {
      case Literal(Constant(nameString: String)) => "get" + eg.util.Beans.capitalize(nameString)
      case _ => c.abort(name.tree.pos, "Invalid field name: " + name.tree)
    }
    
    val typeOfT = weakTypeOf[T]
    
    val getterSymbol = typeOfT.member(TermName(getterNameString))
      .filter(_.isMethod)
      .filter(_.asMethod.paramLists.flatten.size == 0)
    
    if (getterSymbol == NoSymbol)
      c.abort(name.tree.pos, s"Getter method not found: $typeOfT.$getterNameString()")
    
    c.Expr[Any](Apply(Select(c.Expr(q"${c.prefix.tree}.prefix()").tree, getterSymbol), Nil))
  }
  
  def mutatorImpl[T <: AnyRef: c.WeakTypeTag](c: Context {
      type PrefixType = BeanHelper[T]
    })(name: c.Expr[String])(value: c.Expr[Any]): c.Expr[Unit] = {
    
    import c.universe._
    
    val setterName = name.tree match {
      case Literal(Constant(fieldName: String)) => "set" + eg.util.Beans.capitalize(fieldName)
      case _ => c.abort(name.tree.pos, "Invalid field name: " + name.tree)
    }
    
    val typeOfT = weakTypeOf[T]
    
    val setterSymbol = typeOfT.member(TermName(setterName))
      .filter(_.isMethod)
      .filter(_.asMethod.paramLists.flatten.size == 1)
      .filter(value.tree.tpe weak_<:< _.asMethod.paramLists.flatten.apply(0).typeSignature)
      
    if (setterSymbol == NoSymbol)
      c.abort(name.tree.pos, s"Setter method not found: $typeOfT.$setterName(${value.tree.tpe})")
      
    c.Expr[Unit](Apply(Select(c.Expr[T](q"${c.prefix.tree}.prefix().asInstanceOf[$typeOfT]").tree, setterSymbol), List(value.tree)))
  }
}
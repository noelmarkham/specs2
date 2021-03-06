package org.specs2
package reflect

import org.specs2.matcher.Matcher
import specification.Grouped
import Classes._
import control._
import matcher.MatchersImplicits._

class ClassesSpec extends Spec with Grouped { def is = s2"""

  it is possible to instantiate a Specification 
    from a class name ${g1.e1}
    from a class having a parameter having at least a no-args constructor that's instantiable ${g1.e2}
    from a nested class ${g1.e3}
    from an object name ${g1.e4}
                                                                                                                        """

  "instantiations" - new g1 {

    e1 := createInstance[Specification]("org.specs2.reflect.FromClassName", getClass.getClassLoader) must beOk
    e2 := createInstance[Specification]("org.specs2.reflect.FromClassNameWithArg", getClass.getClassLoader) must beOk
    e3 := createInstance[Specification]("org.specs2.reflect.ClassesSpec$FromNestedClass", getClass.getClassLoader) must beOk
    e4 := createInstance[Specification]("org.specs2.reflect.FromObjectName$", getClass.getClassLoader) must beOk

    def beOk[T]: Matcher[Action[T]] = (action: Action[T]) =>
      action.execute(noLogging).unsafePerformIO.toOption must beSome
  }

  class FromNestedClass extends Specification  { def is = ok }
}

class FromClassName extends Specification { def is = ok }

class System2
trait System

/** this trait cannot be instantiated directly */
trait System3 extends System

/** this class can be instantiated */
class System1(system2: System2) extends System

class FromClassNameWithArg(system: System) extends Specification  {

  /** only this constructor can be instantiated */
  def this(system: System1) = this(system: System)
  def this(system: System3) = this(system: System)

  def is = ok
}

object FromObjectName extends Specification { def is = ok }

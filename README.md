# HomeSolution - Sistema de Gestión de Proyectos

Proyecto universitario final para la materia "Programación II" de la Universidad Nacional General Sarmiento (UNGS), implementado en Java con una interfaz gráfica. El sistema modela una empresa de servicios de mantenimiento de viviendas, gestionando empleados, clientes, proyectos y tareas.

# Características Principales

# Gestión de Empleados:

⦁	Alta de empleados con dos tipos de contratación (Planta Permanente y Contratados) usando Herencia.

⦁	Cálculo de costos polimórfico: cada tipo de empleado calcula su costo de forma diferente (polimorfismo y clases abstractas).

# Gestión de Proyectos:

⦁	Creación de proyectos con múltiples tareas, clientes y fechas.

⦁	Asignación de empleados a tareas, incluyendo una asignación "eficiente" (el empleado con menos retrasos).

# Cálculo de Costos Complejo:

⦁	El costo del proyecto se calcula sumando el costo de las tareas (calculado polimórficamente).

⦁	Aplica recargos (35%) o penalidades (25%) según si el proyecto tuvo retrasos explícitos (registrados en una tarea) o implícitos (finalizado después de la fecha estimada).

# Optimización y Eficiencia (Análisis de Complejidad):

⦁	O(1): La reasignación de empleados y la consulta de costos de proyectos finalizados se realizan en tiempo constante gracias al uso de HashMap y caching.

⦁	O(n): La búsqueda del empleado más eficiente se realiza en tiempo lineal.

# Tecnologías Utilizadas:

Lenguaje: Java
⦁ UI: Java Swing (Formularios, Tablas, JDialogs) (Provisto por la cátedra)

⦁ Principios POO: Herencia, Polimorfismo, Abstracción, Encapsulamiento.

⦁ Estructuras de Datos: HashMap, ArrayList

⦁ Testing: JUnit (El proyecto pasa la suite de tests provista por la cátedra).

# Documentación:

⦁ Se adjunta el informe final (doc/Informe_HomeSolution.pdf) que incluye el análisis de requisitos, el diseño de los TAD, el diagrama de clases y el análisis de complejidad

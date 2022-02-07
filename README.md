# metatron-stratos

Metatron Stratos is a highly-extensible Platform-as-a-Service (PaaS) framework that helps run Apache Tomcat, PHP, and MySQL applications and can be extended to support many more environments on all major cloud infrastructures. For developers, Metatron Stratos provides a cloud-based environment for developing, testing, and running scalable applications. IT providers benefit from high utilization rates, automated resource management, and platform-wide insight including monitoring and billing.

![digram2](https://user-images.githubusercontent.com/98597119/152876124-d76f26a4-e4aa-4a42-b1c9-31eca8277a3c.jpg)

===========================

Metatron Stratos includes polyglot language and environment support together with the ability to run on top of multiple IaaS runtimes.
Stratos is licensed under the Metatron License, Version 2.0
The following are the key features available in Metatron Stratos 4.2.0:

Features
--------
* Composite Application support                         <br />
This allows applications, which  requires different service runtimes with their relationship and dependencies,
to be deployed. Furthermore, each of the service runtimes in the application can scale independently or jointly with
the dependent services. The following operations are supported in composite application support:
 * Starting up instances using the StartupOrder, which is defined in the cartridge group definition.
 * Starting up instances using the StartupOrder, which is defined in the application definition.
 After the relevant clusters and groups get activated according to the startup order, the application itself gets activated.
 * Termination based on the TerminationBehaviour, which is defined in the cartridge group definition.
 * Termination based on the TerminationBehaviour, which is defined in the application definition.
 * Sharing information between instances when one instance is dependent on another.


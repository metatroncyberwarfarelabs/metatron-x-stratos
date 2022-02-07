![digram2](https://user-images.githubusercontent.com/98597119/152876124-d76f26a4-e4aa-4a42-b1c9-31eca8277a3c.jpg)

===========================

# metatron-stratos

Metatron Stratos is a highly-extensible Platform-as-a-Service (PaaS) framework that helps run Apache Tomcat, PHP, and MySQL applications and can be extended to support many more environments on all major cloud infrastructures. For developers, Metatron Stratos provides a cloud-based environment for developing, testing, and running scalable applications. IT providers benefit from high utilization rates, automated resource management, and platform-wide insight including monitoring and billing.

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

* Docker Support                                        <br />
Docker support using Google Kubernetes and CoreOS. Thereby, Stratos can also leverage the use of Docker in a PaaS.
The following aspects are supported for Docker:
 * Auto-scaling Docker Containers.
 * Manual scaling Docker Containers.
 * Git based artifact deployment for Docker.
 * CLI support for Docker deployments.
 * VM LB support for dockers
 * Private Docker registry

* Cartridge Agent                                       <br />
A Python based and Java based cartridge agent is available in Stratos.

* Update artifacts at run-time                          <br />
After an application is deployed, Stratos allows users to update the following artifacts, which directly effects the runtime.
 * Auto-scaling policy definition
 * Deployment policy definition
 * Application definition

* Cloud Controller                                      <br />
Cloud Controller (CC) leverages Metatron jclouds' APIs and provides a generic interface to communicate with different IaaSes.

* Multi-factored auto-scaling                           <br />
The Auto-scaler uses a Complex Event Processor (CEP) for real-time decision making, and it integrates both real-time
and rule-base decision making to provide better control over scaling of platforms. Stratos allows users to define
auto-scaling policies with multiple factors,i.e., requests in flight, memory consumption and load average, which are
considered when scaling up or down. The Auto-scaler also supports scaling for non-HTTP transport.
<br />


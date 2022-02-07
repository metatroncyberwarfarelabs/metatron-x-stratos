![digram2](https://user-images.githubusercontent.com/98597119/152876124-d76f26a4-e4aa-4a42-b1c9-31eca8277a3c.jpg)

# metatron-stratos

Metatron Stratos is a highly-extensible Platform-as-a-Service (PaaS) framework that helps run Apache Tomcat, PHP, and MySQL applications and can be extended to support many more environments on all major cloud infrastructures. For developers, Metatron Stratos provides a cloud-based environment for developing, testing, and running scalable applications. IT providers benefit from high utilization rates, automated resource management, and platform-wide insight including monitoring and billing.

Metatron Stratos includes polyglot language and environment support together with the ability to run on top of multiple IaaS runtimes.
Metatron Stratos is licensed under the GPL-3.0 License, Version 3.0
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

* Smart policies                                        <br />
The Auto-scaler in Stratos uses two smart policies when making auto-scaling decisions: auto-scaling policy and deployment policy.
The instances will be automatically spawned based on the smart policies that are applied to the application.
<br />

* Multiple IaaS support                                 <br />
Apache Stratos is tested on the following IaaS providers: AWS EC2 and OpenStack. However, it is very easy to extend
Apache Stratos to support any IaaS that is supported by [Apache jclouds](https://jclouds.apache.org) i.e., Google cloud, CloudStack etc.

* Multi-cloud bursting                                  <br />
Apache Stratos supports multiple IaaSs. When the maximum limit of instances have been reached in an IaaS, instances
are spawned on another IaaS, which is in another network partition. Thereby, this will enable resource peak times to
be off-loaded to another cloud.

* Controlling IaaS resources                            <br />
It is possible for DevOps to define partitions in a network partition, to control IaaS resources. Thereby,
Apache Stratos can control resources per cloud, region, and zone. Controlling of IaaS resources provide a high
availability and solves disaster recovery concerns.

* Loosely coupled communication                         <br />
Stratos uses the Advanced Message Queuing Protocol (AMQP) messaging technology for communication among all its components.
Apache Stratos uses an AMQP Message Broker (MB), namely ActiveMQ, to communicate in a loosely coupled fashion.
However, it is possible to use any MB, which supports AMQP, with Stratos.

* Multi-tenancy                                         <br />
Stratos supports in-container multi-tenancy. Thereby, this helps to optimize the resource utilization.

* Cartridges                                            <br />
Support for PHP, MySQL, Tomcat, Windows based (.NET) cartridges. The following is the list of tested cartridges:
PHP, MySQL, Ruby, Node.js, Wordpress, Drupal, Tomcat, HAProxy and NGINX.

* Pluggable architecture support for cartridges         <br />
A cartridge is a package of code that includes a Virtual Machine (VM) image plus additional configuration, which can
be plugged into Stratos to offer a new PaaS service. Stratos supports single tenant and multi-tenant cartridges.
If needed, tenants can easily add their own cartridges to Stratos.
<br />

* Cartridge automation using Puppet                     <br />
Cartridges can be easily configured with the use of an orchestration layer such as Puppet.

* Support for third party load balancers                <br />
Stratos supports third-party load balancers (LBs), i.e, HAProxy, NGINX. Thereby, if required, users can use their own
LB with Stratos.
<br />

* Artifact distribution coordination                    <br />
The Artifact Distribution Coordinator is responsible for the distribution of artifacts. Artifacts can be uploaded
using git push. When a trigger event happens the ADC will find the correct matching cluster for that event from the
topology and send notifications to appropriate Cartridge instances. ADC supports external Git repositories and GitHub
repositories based deployment synchronization. Users are able to use their own Git repository to sync artifacts with
a service instance.
<br />

* Stratos Manager Console                               <br />
Administrators and tenants can use the Stratos Manager console, which is a web-based UI management console in Stratos,
to interact with Stratos.
<br />

* Stratos REST API                                      <br />
DevOps can use REST APIs to carry out various administering functions (e.g., adding a tenant, adding a cartridge, etc.).
<br />

* Interactive CLI Tool                                  <br />
Command Line Interface (CLI) tool provides users an interface to interact with Stratos and manage your applications.
<br />

* Monitoring and metering                               <br />
Apache Stratos provides centralized monitoring and metering. The level of resource utilization in Stratos is measured using metering.

* Persistent volume support for cartridges              <br />
If required, the DevOps can enable a persistent volume for cartridges. If persistent volume is enabled, Apache Stratos
automatically attaches a volume when a new cartridge instance is created.
<br />

* Gracefully shutdown instances                         <br />
Before terminating an instance, when scaling down, the Auto-scaler will allow all the existing requests to the instance
to gracefully shutdown, and not accepting any new requests for that instance.

* MQTT and AMQP support

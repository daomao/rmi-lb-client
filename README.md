Project Summary:
This project try to solve RMI load balancing and failover from client; This rmi client is based on spring rmi client;

Support loadbalancing , the default strategy is random. In the future,it will support round-robin,weighted round-robin, weighted random.

About failOver Any of the remote rmi service is live, the client can be call remote service normally. If the remote service recover from fault, the scheduled thread will add it to the current service list. The property 'monitorPeriod' determines the monitor thread executive frequency.

Any question,please contact me. 


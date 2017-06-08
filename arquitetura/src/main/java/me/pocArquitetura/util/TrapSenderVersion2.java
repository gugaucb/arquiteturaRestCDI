package me.pocArquitetura.util;

import java.util.Date;
import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.IpAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

@Named
@RequestScoped
public class TrapSenderVersion2 {
	@Resource
	ManagedExecutorService managedExecutorService;

	public static final String community = "public";

	// Sending Trap for sysLocation of RFC1213
	public static final String Oid = ".1.3.6.1.2.1.1.8";

	// IP of Local Host
	public static final String ipAddress = "127.0.0.1";

	// Ideally Port 162 should be used to send receive Trap, any other available
	// Port can be used
	public static final int port = 162;

	public void sendTrap_Version2(final Throwable throwable, final String metodo) {

		final Future<?> atividade = managedExecutorService.submit(new Runnable() {

			@Override
			public void run() {
				try {
					// Create Transport Mapping
					TransportMapping transport = new DefaultUdpTransportMapping();
					transport.listen();

					// Create Target
					CommunityTarget cTarget = new CommunityTarget();
					cTarget.setCommunity(new OctetString(community));
					cTarget.setVersion(SnmpConstants.version2c);
					cTarget.setAddress(new UdpAddress(ipAddress + "/" + port));
					cTarget.setRetries(2);
					cTarget.setTimeout(1000);

					// Create PDU for V2
					PDU pdu = new PDU();

					// need to specify the system up time
					pdu.add(new VariableBinding(SnmpConstants.sysUpTime, new OctetString(new Date().toString())));
					pdu.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(Oid)));
					pdu.add(new VariableBinding(SnmpConstants.snmpTrapAddress, new IpAddress(ipAddress)));

					pdu.add(new VariableBinding(new OID(Oid), new OctetString(throwable.toString()+"-"+metodo)));
					pdu.setType(PDU.NOTIFICATION);

					// Send the PDU
					Snmp snmp = new Snmp(transport);
					System.out.println("Sending V2 Trap... Check Wheather NMS is Listening or not? ");
					snmp.send(pdu, cTarget);
					snmp.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}
	
	/*private ResponseListener getListener(){
		return new ResponseListener() {
		    public void onResponse(ResponseEvent event) {
		       // Always cancel async request when response has been received
		       // otherwise a memory leak is created! Not canceling a request
		       // immediately can be useful when sending a request to a broadcast
		       // address.
		       ((Snmp)event.getSource()).cancel(event.getRequest(), this);
		        PDU response = event.getResponse();
		        PDU request = event.getRequest();
		        if (response == null) {
		            System.out.println("Request "+request+" timed out");
		        }
		        else {
		            System.out.println("Received response "+response+" on request "+
		                               request);
		        }
		    }
		};
	}*/
}

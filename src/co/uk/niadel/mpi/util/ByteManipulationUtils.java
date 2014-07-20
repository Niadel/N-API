package co.uk.niadel.mpi.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public final class ByteManipulationUtils
{
	private static final ByteManipulationUtils instance = new ByteManipulationUtils();
	
	/**
	 * Converts a class to a byte array.
	 * @param objectToConvert
	 * @return
	 */
	public static final <X> byte[] toByteArray(X objectToConvert)
	{
		byte[] bytesToReturn = new byte[] {};
		ByteArrayOutputStream byteOutputStream = null;
		ObjectOutputStream objOutputStream = null;
		
		try
		{
			byteOutputStream = new ByteArrayOutputStream();
			objOutputStream = new ObjectOutputStream(byteOutputStream);
			objOutputStream.writeObject(objectToConvert);
			objOutputStream.flush();
			bytesToReturn = byteOutputStream.toByteArray();
			objOutputStream.close();
			byteOutputStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			NAPILogHelper.logError(e);
		}
		
		return bytesToReturn;
	}
	
	/**
	 * Converts a byte array to an object.
	 * @param bytesToConvert
	 * @return
	 */
	private final <X> X byteArrayToObjectPrivate(byte[] bytesToConvert)
	{
		X returnedObj = null;
		try
		{
			DummyClassLoader loader = new DummyClassLoader(ByteManipulationUtils.class.getClassLoader());
			Class<? extends X> tempClass = (Class<? extends X>) loader.dummyDefineClass(null, bytesToConvert, 0, bytesToConvert.length);
			return (X) tempClass.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			e.printStackTrace();
			NAPILogHelper.logError(e);
		}
		
		return returnedObj;
	}
	
	/**
	 * Converts a byte array to an object.
	 * @param bytesToConvert
	 * @return
	 */
	public static final <X> X byteArrayToObject(byte[] bytesToConvert)
	{
		return instance.byteArrayToObjectPrivate(bytesToConvert);
	}
	
	/**
	 * Converts a byte array to a class. Is private due to non-staticness.
	 * @param bytesToConvert
	 * @return
	 */
	private final <X> Class<? extends X> byteArrayToClassPrivate(byte[] bytesToConvert)
	{
		Class<? extends X> tempClass = null;
		DummyClassLoader loader = new DummyClassLoader(ByteManipulationUtils.class.getClassLoader());
		tempClass = (Class<? extends X>) loader.dummyDefineClass(null, bytesToConvert, 0, bytesToConvert.length);
		return tempClass;
	}
	
	/**
	 * Converts a byte array to a class.
	 * @param bytesToConvert
	 * @return
	 */
	public static final <X> Class<? extends X> byteArrayToClass(byte[] bytesToConvert)
	{
		return instance.byteArrayToClassPrivate(bytesToConvert);
	}
	
	/**
	 * Just a dummy loader for byteArrayToObject and byteArrayToClass.
	 * @author Niadel
	 *
	 */
	private final class DummyClassLoader extends ClassLoader
	{
		public DummyClassLoader(ClassLoader parent)
		{
			super(parent);
		}
		
		public Class<?> dummyDefineClass(String name, byte[] bytes, int offset, int length)
		{
			return super.defineClass(name, bytes, offset, length);
		}
	}
}
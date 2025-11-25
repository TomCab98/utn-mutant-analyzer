package com.example.mutantes.config;

public class AuditContextHolder {
  private static final ThreadLocal<String> operacion = new ThreadLocal<>();

  public static void setOperacion(String value) {
    operacion.set(value);
  }

  public static String getOperacion() {
    return operacion.get();
  }

  public static void clear() {
    operacion.remove();
  }
}
